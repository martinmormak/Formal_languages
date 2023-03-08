#include <stdio.h>
#include <string.h>
#include "parser.h"

extern int state;

int main(){
    printf("Enter your input string: ");
    char inputString[1000];
    scanf("%s",inputString);
    int countParsedString=parseString(inputString);
    printf("I found %d parsed strings from your input string.\n",countParsedString);
    return 0;
}
