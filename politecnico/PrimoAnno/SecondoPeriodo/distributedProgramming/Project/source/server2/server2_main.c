
#include    <stdlib.h>
#include    <string.h>
#include    <inttypes.h>
#include    "../errlib.h"
#include    "../sockwrap.h"
#include    "../utilities.h"
#include   <sys/stat.h>
#include   <sys/types.h>
#include   <fcntl.h>
#include <sys/sendfile.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <time.h>
#include <string.h>
#include <signal.h>
#include <netinet/in.h>

/*CONSTANTS DEFINITION*/
#define RBUFLEN		128 /* Buffer length */
#define DIM 20
#define ERROR -1 /*constant returned by all the functions in case of error*/
#define MAX_FILE_LENGTH 20
#define CLIENT_SERVED 1


/* FUNCTION PROTOTYPES */
//int mygetline(char *line, size_t maxline, char *prompt);
void service(int s);
int myService(int s);
void sendErr(int s);
int getFileName(int s,char*filename);
int mysendFile(int conn_s,char* filename);
void signal_sigchld_handler(int sig);
void sigintHandler(int sig){
      return;
}


/* GLOBAL VARIABLES */
char *prog_name;

int main(int argc, char *argv[])
{
    int		conn_request_skt,pid;	/* passive socket with pid for fok()*/
    uint16_t 	lport_n, lport_h;	/* port used by server (net/host ord.) */
    int		bklog = 4;		/* listen backlog */
    int	 	s;			/* connected socket */
    socklen_t 	addrlen;
    struct sockaddr_in 	saddr, caddr;	/* server and client addresses */ 
    //signal(SIGINT,sigintHandler);
     signal(SIGPIPE, signal_sigpipe_handler); //registro handler
     signal(SIGCHLD,signal_sigchld_handler);

    prog_name = argv[0];

    if (argc != 2) {
	printf("Usage: %s <port number>\n", prog_name);
	exit(1);
    }

    /* get server port number */
    if (sscanf(argv[1], "%" SCNu16, &lport_h)!=1)
	err_sys("Invalid port number");
    lport_n = htons(lport_h);

    /* create the socket */
    printf("creating socket...\n");
    s = Socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    printf("done, socket number %u\n",s);

    /* bind the socket to any local IP address */
    bzero(&saddr, sizeof(saddr));
    saddr.sin_family      = AF_INET;
    saddr.sin_port        = lport_n;
    saddr.sin_addr.s_addr = INADDR_ANY;
    showAddr("Binding to address", &saddr);
    Bind(s, (struct sockaddr *) &saddr, sizeof(saddr));
    printf("done.\n");

    /* listen */
    printf ("Listening at socket %d with backlog = %d \n",s,bklog);
    Listen(s, bklog);
    printf("done.\n");

    conn_request_skt = s;
     
   
    /* main server loop */
    for (;;)
    {
	/* accept next connection */
	addrlen = sizeof(struct sockaddr_in);
	s = Accept(conn_request_skt, (struct sockaddr *) &caddr, &addrlen);
	if(s==-1){ continue;}
	    pid=fork();
	    if(pid<0){
	        printf("fork() failed\n");
	        close(s);
	        continue;
	    }
	 //father
	    if(pid>0){
	       printf("*********child with pid %d has been created***************\n",pid);
	       close(s);
	    }else{//child
	       close(conn_request_skt);
	       showAddr("Accepted connection from", &caddr);
	       printf("new socket: %u\n",s);
	       service(s);
	       exit(0);
	    }
	
    }
}
/* service is used as a wrapper function which calls myService that returns ERROR in case of error during the service*/
void service(int s) {
       
        int res = myService(s);
        if(res == ERROR){
            sendErr(s);
        }
        close(s);
	printf("******************END and Socket %d closed******************\n\n", s);
        return ;	
}

int myService(int s){
    char cmd_get[5]={'G','E','T',' ','\0'};
    char buf[RBUFLEN]="";		/* reception buffer */
    int n,res;
    char filename[MAX_FILE_LENGTH];
     
     //strutture per gestire select
    fd_set readSet; //insiemi di socket su cui si vuole verificare la condizione di lettura
    int sel_cond = 0; //usato per return select
    struct timeval tval;
    int time;

          
    
   do{  
     
            /*
             * Setup select: 
           */
             FD_ZERO(&readSet); //initialise the set
             FD_SET(s, &readSet); //insert s in readSet
          time = WAIT_TIME; tval.tv_sec = time; tval.tv_usec = 0; //WAIT_TIME for the select
        if( (sel_cond = Select(FD_SETSIZE, &readSet, NULL, NULL, &tval )) == ERROR) {
               printf("ERROR in myService: select failed.\n");
               return ERROR;
         }
        if(sel_cond==0){
               printf("NO REQUEST from the client after %d second so we close the connection\n",time);
              return TIMEOUT_REACHED;
         }
         printf("***select return value %d********\n",sel_cond);
        n=recv(s, buf,4, 0);
        printf("this should be the string 'GET '= %s received from client\n",buf);
      
       if (n < 0)
	{
	       printf("Read of GET error\n");
	       return ERROR;
	}
	else if (n==0)
	 {
	       printf("Connection closed by party on socket %d\n",s);
	       return 1;
	 }
	 buf[4]='\0';
	 if(strcmp(buf,cmd_get) != 0){
	 printf(" WRONG GET: expeted=%s received=%s\n",cmd_get,buf);
	    return ERROR;
	 }
	 /*i try to read the filename sent by the client*/
	    
           res=getFileName(s,filename);
           
           if(res==ERROR){
           printf("Error while trying to read the filename sent by the client\n");
	         return ERROR;
           }
           else{
               if(res==0){
                printf("unable to read the file name\n");
                return 0;
               }
           }
           printf("the requested filename is %s \n",filename);
           
           res=mysendFile(s,filename);
           if(res==ERROR){ 
              printf("Error accured while sending the file\n");
	         return ERROR;
           }
           
           printf("======================================================================\n\n");
           
    } while( sel_cond != 0);
    
      printf("****************END of service and the file %s has been served**************\n",filename);
      return 1;
}

int getFileName(int s,char *filename){
printf("enters filename\n");
     char c;
     int pos=0,nread;
     int select_cond=0;
     //i should find a way to handle the case where the 2 last carachters are \r \n
            do {
            
                 if( (select_cond = mySelect(s)) == -1) {
                    printf("ERROR in the client: select failed.\n");
                    return -1;
                 }
                 if(select_cond==0){
                    printf("UNABLE to read the fileName after the TIMEOUT so will close the connection\n");
                    return 0;
                 }
                nread = recv(s, &c, 1, 0); //leggi un carattere
                if( nread != 1){
                      if(nread == 0){
                         return 0;// in this case the connection has been closed by party
                       }else{
                          return ERROR;// genric error.
                       }
                    

                } else if(nread == 1 && c != '\r' && c != '\n'){
                    //aggiungo carattere solo se fa parte del nome del file (comunque leggo i restanti per pulire buffer)
                    filename[pos++] = c;
                }
            } while( c != '\n' );
            
            
       filename[pos]='\0';//i appenf the \0 carachter in order to be able to use string functions such as strlen() ect...
       printf("returns filename\n");
  return 1;

}

/***************
 *	mysendFile
 * 
 *	Invia il file implementando la risposta al comando "GET filename", con codifica binaria
 */
int mysendFile(int conn_s,char* filename){

    struct stat fileInfo;
    uint32_t lastMod, size;
    size_t filesize, totread = 0, lastread;
    char respHeader[5] = {'+', 'O', 'K', '\r', '\n'}; 
    char buff[BLOCK_SIZE]="";

    FILE* fp=NULL;

    /*
     * check the existence of the file on disk,if not present we send -ERR
     */
          if( (fp = fopen(filename, "r")) == 0){
               printf("Error encountered while opening the file %s \n",filename);
               return ERROR;

          } else printf("SUCCEDED opening the file %s and starting sending operation......\n",filename);

    /*
     *i prepare the header of the reponse
     */

    //retrieve dimenssion and last modification time
         if( stat(filename, &fileInfo) == ERROR ){
              return ERROR;
         }
               lastMod = (uint32_t)fileInfo.st_mtime; //8 BYTE integer value casted to 4
               size = (uint32_t)fileInfo.st_size; //8 BYTE integer value casted to 4
               filesize = (size_t)size;

            printf("The size and last modification time of the requested file are: %lu (%ld bytes), %lu (%ld bytes).\n",
           (unsigned long)size, sizeof(size), (unsigned long)lastMod, sizeof(lastMod));

    // convertion to NBO su 4 byte (htonl) ed inserimento nel buffer respHeader
              lastMod = htonl(lastMod);
              size = htonl(size);
   

    //send buffer respHeader 
        if( writen(conn_s, respHeader, sizeof(respHeader)) != sizeof(respHeader) ) {
              printf("ERROR in send file while sending the header\n");
              return ERROR;
         }
         
        if(writen(conn_s, &size, sizeof(size)) != sizeof(size) ) {
              printf("ERROR in send file while sending the file size %d\n",size);
              return ERROR;
        }

    /*
     * i read block file of size Blocksieze and i send them
     */
         
        while(totread != filesize )
        {
        //read a block
               lastread = readBlock(fp, buff, BLOCK_SIZE);
               totread += lastread;
        //send the block on the socket
        
           if( lastread < BLOCK_SIZE ){
            //sending the last block of the file
              if( writen(conn_s,buff, lastread) != lastread ) {
                printf("ERROR sending a chunk to the client\n");
                return ERROR;
              }

           } else {
            //sending a block
             if( writen(conn_s,buff, lastread) != lastread) {
                printf("ERROR sending a chunk o the client\n");
                return ERROR;
             }
        }
      }
               if( writen(conn_s,&lastMod,sizeof(lastMod)) != sizeof(lastMod)){
                    printf("ERROR writting the last modification date of the requested file %s\n",filename);
                    return ERROR;
               }
               if (fclose(fp) == EOF) {                        //chiudi filedescriptor
                    printf("ERROR closing the file descriptor\n");
                    return ERROR;
               }

    printf("FILE correctly sent to the client\n");
    return CLIENT_SERVED;
}

void sendErr(int conn_s) {

    char err_cmd[6] = {'-', 'E', 'R', 'R', '\r', '\n'};
    ssize_t nsend;
  
    if((nsend = writen(conn_s, err_cmd, 6)) != 6){
      printf("[error] sendErrMsg failed. returning to main process.\n");
            return;
    }

    printf("Error message sent correctly.\n");
    return;
}

void signal_sigchld_handler(int sig){
 int ret1;
 pid_t pid;
 
  while((pid = waitpid(-1, &ret1, WNOHANG))> 0)
   printf("child %d terminated \n", pid);
 /*ret1=wait(NULL);
  printf("**************the child with pid %d has terminated\n",ret1);*/
 return ;
}
