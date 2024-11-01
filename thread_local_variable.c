#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<pthread.h>

static __thread int glob = 0;
static void *incr(void *loop){
	int loc,j;
	for(j = 0; j< *(int *)loop; j++){
		loc=glob;
		loc++;
		glob=loc;
	}
	printf("thread_ID= %lu,glob= %d\n",pthread_self(),glob);
	pthread_exit(NULL);
}
int main(){
	int p1 = 50, p2 = 100;
	//before
	printf("(main thread) glob value after run 2 threads %d\n", glob);

	pthread_t id[2];
	pthread_create(&id[0],NULL,incr,&p1);
	pthread_create(&id[1],NULL,incr,&p2);
	
	pthread_join(id[0],NULL);
	pthread_join(id[1],NULL);
	//after
	printf("(main thread) glob value after run 2 threads %d\n", glob);

	return 0;
}
