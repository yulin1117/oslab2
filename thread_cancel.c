#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

// 執行緒函式：計算兩個整數的總和
void *child(void *arg) {
    // 將參數轉回整數陣列指標
    int *input = (int *)arg;

    // 為結果分配記憶體（存一個 int）
    int *result = malloc(sizeof(int));
    if (result == NULL) {
	    printf("mallocerror");
	    perror("Failed to allocate memory");
	    pthread_exit(NULL);
    }

    // 計算 input[0] + input[1]，並將結果存入 result[0]
    result[0] = input[0] + input[1];
    // printf("result=%d\n",result[0]);
    for(int i=1; i<=3 ; i++){
    	sleep(1);
	printf("sleep: %d\n",i);
    }
    //printf("result=%d\n",result[0]);
    // 執行緒結束，返回結果指標
    pthread_exit((void *)result);
}

int main() {
    pthread_t t;  // 宣告執行緒變數
    void *ret;    // 用來接收執行緒返回的結果
    int input[2] = {1, 2};  // 初始化兩個整數作為參數

    // 建立執行緒，傳遞參數 input 給 child 函式
    pthread_create(&t, NULL, child, (void *)input);

    //cancel the child process
    int r = pthread_cancel(t);
    // 等待執行緒結束，取得返回值
    pthread_join(t, &ret);
    if(r){
    printf("pthread_cancel succesfully\n");}
    //將返回的結果轉回整數指標
    int *result = (int *)ret;

    //輸出計算結果
    printf("main thread:\n");
    printf("%d + %d = %d\n", input[0], input[1], *result);

    //釋放分配的記憶體
    free(result);

    return 0;
}
