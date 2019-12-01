
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

#define BUFLEN	128 /* BUFFER LENGTH */
#define OK 1
//valori ritornati da readHeader_A
#define RESPONSE_OK 10
#define RESPONSE_ERR 11
ssize_t totread = 0;
int connected = 0;
/* FUNCTION PROTOTYPES */
int sendGET( int s, char* filename);
int readHeader( int s,int* filesize );
void sig_alarm_handler(int sig);




/* GLOBAL VARIABLES */
char *prog_name;


int main(int argc, char *argv[])
{
 
 /*correctness check of the arguments passed on the command line.*/
  if(argc < 4){
   printf("usage of %s: <address> <port> <file1> <file2> .......\n",argv[0]);
   exit(1);
  }
  
    int filesize,lastMod;
    char   rbuf[BLOCK_SIZE]="";		/*Reception buffer used to read BLOCK_SIZE BYTES from the server socket at a time */
    FILE *fp=NULL;
    uint16_t tport_n, tport_h;	/* server port number (net/host ord) */
    int	s,i;
    int	result;
    struct sockaddr_in	saddr;		/* server address structure */
    struct in_addr	sIPaddr; 	/* server IP addr. structure */
     
     //strutture per gestire select
    fd_set readSet; //insiemi di socket su cui si vuole verificare la condizione di lettura
    int sel_cond = 0; //used as return value for the select
    struct timeval tval;
    int time;

    prog_name = argv[0];
    //signal declaration to handle wait time of select
    signal(SIGALRM,sig_alarm_handler);
    
    result = inet_aton(argv[1], &sIPaddr);
    if (!result)
	err_quit("Invalid address");

    /*convert the port number*/
    if (sscanf(argv[2], "%" SCNu16, &tport_h)!=1)
	err_quit("Invalid port number");
    tport_n = htons(tport_h);

    /* create the socket */
    printf("Creating socket\n");
    s = Socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    printf("done. Socket fd number: %d\n",s);

    /* prepare address structure */
    bzero(&saddr, sizeof(saddr));
    saddr.sin_family = AF_INET;
    saddr.sin_port   = tport_n;
    saddr.sin_addr   = sIPaddr;

    /* connect */
    showAddr("Connecting to target address", &saddr);
    
    /*USed to exit of the connect did not succeed after WAIT_TIME seconds*/
    alarm(WAIT_TIME); 
    Connect(s, (struct sockaddr *) &saddr, sizeof(saddr));
     
     connected=1;//used by the sig_alarm_ahndler to check if the host succesfully connected or not after the TIME_OUT
     printf("done.\n");
     
    /* main client loop */
	
    for (i=3 ;i<argc;i++ )
    {
      /* Send the command get to the server*/
	 result=sendGET(s,argv[i]);
	 
	    if(result == -1){
	      printf("ERROR sending the command GET for file %s\n",argv[i]);
	      exit(EXIT_FAILURE);
	    }
	      printf("GET sent and waiting for response\n");	
        /*
          * Setup select select: 
        */
        
               FD_ZERO(&readSet); //initialisation
               FD_SET(s, &readSet); //put s in the readSet
               time = WAIT_TIME; tval.tv_sec = time; tval.tv_usec = 0; //TIMEOUT for waiting the server reponse
           if( (sel_cond = Select(FD_SETSIZE, &readSet, NULL, NULL, &tval )) == -1) {
               printf("ERROR in the client: select failed.\n");
               return -1;
           }
           if(sel_cond==0){
               printf("NO RESPONSE from the server after %d seconds so we close the connection\n",time);
              return TIMEOUT_REACHED;
           }
           
       /*i read the header of the server's answer by calling this function which also returns the filesize*/
              
              
	      result = readHeader(s,&filesize);
	   if(result==RESPONSE_ERR){
	       
	       printf("an error occured trying to download the file %s : it may not exist on the server\n",argv[i]);
	      //continue; //this file may not exist so i continue by requesting another file
	      break;
	   }
	   if(result == TIMEOUT_REACHED || result == -1){
	      printf("BAD header sebt by the server for file %s\n",argv[i]);
	   }
	
      /*i read the content of the file sent by the server and save localy*/
      	
	  if ((fp = fopen(argv[i], "w")) == NULL){
	         printf("***ERROR**** opening the file %s so we close the connection.\n",argv[i]);
	         break;// better because the server closed the connection so it's useless to go ahead
	  }
            while(totread != filesize){
        	 
        	    if( (sel_cond = Select(FD_SETSIZE, &readSet, NULL, NULL, &tval )) == -1) {
                           printf("ERROR in the client: select failed.\n");
                           return -1;
                    }
                   if(sel_cond==0){
                          printf("NO RESPONSE from the server after %d seconds so we close the connection\n",time);
                         return TIMEOUT_REACHED;
                   }
	           if ( readBlockAppend(s, fp, rbuf, filesize, &totread,argv[i]) == -1) {
	             printf("[error] readBlockAppend : unable to read all the necessary byte for file %s.\n",argv[i]);
			break;
	           }
             }	
   
           //check if i received all the file
	          if (totread == filesize) {
		      fclose(fp); 
	              fp = NULL;
		      totread = 0;
		    /* i read the the last modification time of the file*/
                      
                      if( (sel_cond = Select(FD_SETSIZE, &readSet, NULL, NULL, &tval )) == -1) {
                           printf("ERROR in the client: select failed.\n");
                           return -1;
                      }
                      if(sel_cond==0){
                           printf("NO RESPONSE from the server after %d seconds so we close the connection\n",time);
                           return TIMEOUT_REACHED;
                      }
	              result = Recv(s,(void*)(&lastMod),4,0);
	                if(result != 4){
	                  printf("ERROR:unable to read the bytes related to the last modification time\n");
	                      break;
	                }
	                    lastMod=ntohl(lastMod);
	                    printf("file name %s \n",argv[i]);
	                    printf("file dimension: %d.\n ",filesize );
	                    printf("file last modification time %d\n ",lastMod);
	                    filesize = 0;//i can even remove this				
	         }else{
	          printf("did not received all the bytes related to file %s",argv[i]);
	          break;
	         }
     
	printf("===========================================================\n");
    }
     close(s);
     exit(0);
}

/*Used to buid and  send the command "GET " to the server*/
int sendGET( int s, char* filename) {
	
	char get_prefix[4] = {'G', 'E', 'T', ' '};
	char get_suffix[2] = { '\r', '\n'};
        size_t len = strlen(filename); 

        char* cmd = malloc( sizeof(char) * (4 + len + 2) ); //"GET " + lunghezza stringa + "\r\n"

              if(cmd == NULL){
                     printf("[error] error in malloc, in sendCmdGET_A.\n");
                     exit(EXIT_FAILURE);
               }
	
    //concatenazione elementi per formare comando da inviare
   
                    memset(cmd, 0, 4+len+2); //initialise buffer to all zeroes
                    memcpy(cmd, get_prefix, 4); //copy "GET "
                    memcpy(cmd+4, filename, len); //copy filename
                    memcpy(cmd+4+len, get_suffix, 2); //copy "\r\n"
    
    //invio comando sul socket tramite protocollo ASCII
               if (writen(s, cmd, len + 4 + 2) != (len+4+2)) {//invio comando GET al server
                     printf("[debug] error while sending cmmand get for file %s.try again.\n", filename);
                     return -1;
               }
	free(cmd); //free buffer from heap memory
	
    return OK;
}


/**********************************************************************************************************************
 * Read the response header sent by server and return RESPONSE_OK or REPONSE_ERR depending on if the server answered with OK or -ERR
 */
int readHeader( int s, int* filesize ){
   
    char cmd_ok[6] = {'+','O','K','\r','\n', '\0'};
    char cmd_response[6] = "";
    char cmd_type;
    
    //strutture per gestire select
    int sel_cond = 0; //used as return value for the select
    
    

    //Read the first carachter
             Recv(s, &cmd_type, 1,0); //i only read the first byte to see if it's + or -
    // Read the rest of the comand according to the first character
                
                //setting up the select to avoid infinite wait
               
                if( (sel_cond = mySelect(s)) == -1) {
                    printf("ERROR in the client: select failed.\n");
                    return -1;
                }
                if(sel_cond==0){
                    printf("NO RESPONSE from the server after the TIMEOUT so we close the connection\n");
                    return TIMEOUT_REACHED;
                }
          if(cmd_type == '+'){

              //+OK\r\n <=> 5 chars , one has already been read 
                cmd_response[0] = '+';
                Recv(s, (void*)(cmd_response+1), 4,0);
                cmd_response[5] = '\0';

          } else if(cmd_type == '-'){
  
              //-ERR\r\n <=> 6 chars, one has already been read 
               cmd_response[0] = '-';
               Recv(s, cmd_response+1, 5,0);
               cmd_response[6] = '\0';
           
         }


    /*
     * check the received command in order to return the correct value
     */
     
     
         if(strcmp(cmd_response,"-ERR\r\n\0") == 0){

             printf("received -ERR dal server. \n");
             return RESPONSE_ERR; //cancel the download of this file and close the connection


         } else if (strcmp(cmd_response, cmd_ok) == 0){
		
		printf("received command +OK. going ahead to read file dimensiion and last modification time: \n");
        
             //I read the next 4 bytes that contains the file dimession
             
                 if( (sel_cond = mySelect(s)) == -1) {
                    printf("ERROR in the client: select failed.\n");
                    return -1;
                }
                if(sel_cond==0){
                    printf("NO RESPONSE from the server after the TIMEOUT so we close the connection\n");
                    return TIMEOUT_REACHED;
                }
		Recv(s, (void*)filesize, 4,0); // 4 <=> B1 B2 B3 B4
		*filesize = ntohl(*filesize); //converto da big endian a little endian
		

		return RESPONSE_OK;

        }else{
		
	printf("[error] received unexpected command %s with length %ld  from server and exiting.\n",cmd_response,strlen(cmd_response));
               exit(EXIT_FAILURE);            
        }

}

/* Used to handle the case the host tries to connect to an address that does not exist.*/
void sig_alarm_handler(int sig){
        if(connected==0){
            printf("The host did not succed to connect after the time out so we exit: the input address may be invalid\n");
            exit(EXIT_FAILURE);
   
   }
}
