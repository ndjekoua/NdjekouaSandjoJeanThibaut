/*
 * Includes
 */
#include <arpa/inet.h> /*libreria richiest per check su correttezza IP, inet_aton ecc..*/
#include <stdlib.h> // getenv()
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h> // timeval
#include <sys/select.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <sys/un.h> // unix sockets
#include <netdb.h>
#include <errno.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>
#include <netdb.h>

//aggiunti per INADRR_ANY
#include <netinet/in.h>
#include <netinet/ip.h>


//import dei prototipi necessari
#include "utilities.h"
#include "sockwrap.h"
#include "errlib.h"

/***************************************************************************************
*                  Wrapper of functions for l'I/O da FILE                            *
****************************************************************************************/


size_t readBlock(FILE* fp, char* chunk, size_t nbytes){

    size_t read_size = 0;
    if(fp){
        read_size = fread(chunk, sizeof(char), nbytes, fp);
    }
	
	if(ferror(fp)){
		printf("[error] unable to read chunk from file. exiting with failure state.\n");
		exit(EXIT_FAILURE);
	}
	
    return read_size; //numero di byte effettivammente letti.
}

void writeChunk(FILE* fp, char* chunk, ssize_t nbytes,char *filename){

    size_t write_size;

    if(fp){

        write_size = fwrite(chunk, sizeof(char), nbytes, fp);

        if(write_size != nbytes){

            //qui si dovrebbe riprovare a scrivere!

            printf("[error] unable to append to file. exiting with failure state.\n");
            exit(EXIT_FAILURE);
        }
    }
    
    return;
}  

    
/*
 * Lread a block from the socket and append to the file
 */
int readBlockAppend(int s, FILE* fp, char* chunk, int filesize, ssize_t* totread,char *filename){

    ssize_t nread;

    memset(chunk, 0, BLOCK_SIZE); //clear the buffer

    if( filesize>= BLOCK_SIZE && *totread+ BLOCK_SIZE>filesize ){

            //reading the last block
            nread = Recv(s, chunk, (size_t)(filesize - *totread),0);

    }else if( filesize >=  BLOCK_SIZE) {

            //reading a random chunk
            nread = Recv(s, chunk,  BLOCK_SIZE,0);

    } else {
            //i read everything the first time
            nread = Recv(s, chunk, filesize,0);
    }


    if( nread>0 ){
            //i read a part of the file so i append
            writeChunk(fp, chunk, nread,filename);
            *totread += nread; //total number o bytes read from the socket

    } else return -1;

    return nread; //if i read and wrote correctly a block of the file
}


int mySelect(int s){

  //strutture per gestire select
    fd_set readSet; //insiemi di socket su cui si vuole verificare la condizione di lettura
    int sel_cond = 0; //used as return value for the select
    struct timeval tval;
    int time;
 
   //setting up the select to avoid infinite wait
               FD_ZERO(&readSet); //initialisation
               FD_SET(s, &readSet); //put s in the readSet
               time = WAIT_TIME; tval.tv_sec = time; tval.tv_usec = 0; //TIMEOUT for waiting the server reponse
                if( (sel_cond = Select(FD_SETSIZE, &readSet, NULL, NULL, &tval )) == -1) {
                    //printf("ERROR in the client: select failed.\n");
                    return -1;
                }
                if(sel_cond==0){
                    //printf("NO RESPONSE from the server after %d seconds so we close the connection\n",time);
                    return 0;
                }
 
  return 10; //which means success;
}

/***************************************************************************************
*                           HANDLER PER I SEGNALI
****************************************************************************************/

//handler per SIGPIPE
void signal_sigpipe_handler(int signum){
	
    printf("Caught signal SIGPIPE (signum=%d). The host closed the connection unexpectedly, maybe it failed.\n",signum);
    fflush(stdout);
}


