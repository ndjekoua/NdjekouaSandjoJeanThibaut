/*
 * necessary libraries
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

//#include "../sockwrap.h"
//#include "../errlib.h"


/*
 * Costanti
 */
#define MAX_PORT 65535 //porta massima
#define MIN_PORT 1025 //porta minima, al di sotto sono utilizzabili solo da root

#define TCP 0 //usate in connectToHost per discriminare il tipo di connessione che vuole creare il client
#define UDP 1


#define BLOCK_SIZE 8000 //will be used by transmission and reception buffer of both clients and server.
#define TRUE 1
#define FALSE 0

#define NOT_VALID 0
#define NOT_CLOSED -1
#define VALID 1
//#define ERROR_ENCOUNTERED -1
#define WAIT_TIME 15
#define TIMEOUT_REACHED 2

/********************************************************************************************************************
 *                                           P R O T O T Y P E S                                                      *
 ********************************************************************************************************************/

//utilità per lettura/scrittura su/da file
void writeChunk(FILE* fp, char* chunk, ssize_t nbytes,char *filename); //write a byte block on file
size_t readBlock(FILE* fp, char* chunk, size_t nbytes); //Read a byte block from file
int mySelect(int s);
//Handler per i segnali
void signal_sigpipe_handler(int signum);


//***TCP*** : Wrapper per delle funzioni di socket.h con gestione degli errori
/*int MyAccept (int listen_sockfd, struct sockaddr* cliaddr, socklen_t *addrlenp);
int MySelect (int maxfdp1, fd_set *readset, fd_set *writeset, fd_set *exceptset, struct timeval *timeout);
ssize_t MyRecv (int fd, void *bufptr, size_t nbytes, int flags);
int MySend (int fd, void *bufptr, size_t nbytes, int flags); //nb: send originale ritorna void*/


//ssize_t readNumBytes (int s, void* buf_ptr, size_t num_bytes); //Legge ""num_bytes"" dal buffer del socket connesso "s" in "buf_ptr"
int readBlockAppend( int s, FILE* fp, char* chunk, int filesize, ssize_t* totread ,char *filename); //legge un blocco di byte dal socket connesso "s"" e li appende al file 



