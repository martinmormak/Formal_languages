#include <string.h>
#include <stdio.h>
#include "parser.h"

int state;

void q0(char c){
    if(c=='a'){
        state=1;
    }else if(c=='b'){
        state=2;
    }else if(c=='c'||c=='d'){
        state=3;
    }else{
        state=-1;
    }
}

void q1(char c){
    if(c=='a'){
        state=1;
    }else if(c=='c'||c=='d'){
        state=3;
    }else{
        state=-1;
    }
}

void q2(char c){
    if(c=='c'||c=='d'){
        state=3;
    }else{
        state=-1;
    }
}

void q3(char c){
    if(c=='a'){
        state=4;
    }else{
        state=-1;
    }
}

void q4(char c){
    if(c=='x'){
        state=5;
    }else{
        state=-1;
    }
}

void q5(char c){
    if(c=='y'){
        state=4;
    }else{
        state=-1;
    }
}

int parser(char s[]){
    for(size_t i=0;i<strlen(s);i++){
        char c=s[i];
        switch(state){
            case 0:{
                q0(c);
                break;
            }
            case 1:{
                q1(c);
                break;
            }
            case 2:{
                q2(c);
                break;
            }
            case 3:{
                q3(c);
                break;
            }
            case 4:{
                if((i+1)<strlen(s)&&s[i]=='x'&&s[i+1]=='y') {
                    q4(c);
                } else{
                    return i;
                }
                break;
            }
            case 5:{
                q5(c);
                break;
            }
            case -1:{
                return-1;
            }
            default:{
                break;
            }
        }
    }
    return (int)strlen(s);
}



/*int parseString(char inputString[]){
    size_t length=strlen(inputString);
    int countParsedString=0;
    for(size_t i=0;i<length;i++){
        char substring[length - i + 1];
        strcpy(substring, &inputString[i]);
        int idx=parser(substring);
        if(state==4){
            printf("Next parsed string: ");
            for(int x=0;x<idx;x++){
                printf("%c",substring[x]);
            }
            printf("\n");
            countParsedString++;
        }
        state=0;
    }
    //printf("%d\n",countParsedString);
    return countParsedString;
}*/

int parseString(char inputString[]){
    size_t length=strlen(inputString);
    int countParsedString=0;
    for(size_t i=0;i<length;){
        char substring[length - i + 1];
        strcpy(substring, &inputString[i]);
        int idx=parser(substring);
        if(state==4){
            printf("Next parsed string [%lld;%lld]: ",i+1,i+idx);
            for(int x=0;x<idx;x++){
                printf("%c",substring[x]);
            }
            printf("\n");
            countParsedString++;
            i=i+idx;
        }else{
            i=i+1;
        }
        state=0;
    }
    //printf("%d\n",countParsedString);
    return countParsedString;
}