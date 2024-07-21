#include <stdio.h>
#include <string.h>
#include "parser.h"

extern int state;

int main(){
    printf("Enter your input string: ");
    char inputString[1000];
    fgets(inputString,1000,stdin);
    int countParsedString=parseString(inputString);
    if(countParsedString!=0) {
        printf("I found %d parsed strings from your input string.\n", countParsedString);
    } else{
        printf("I don't found parse string from your input string.\n");
    }
    return 0;
}
