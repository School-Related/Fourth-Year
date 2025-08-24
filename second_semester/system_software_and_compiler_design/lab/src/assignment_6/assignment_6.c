// commands
// gcc Assignment_6.c -o Assignment_6
// ./Assignment_6
// Enter an arithmetic expression: a+b*c
// Success
// Enter an arithmetic expression: a+*
// Failure
// Enter an arithmetic expression: a++
// Failure

#include <stdio.h>
#include <ctype.h>


char *input;
int index = 0;

void E();
void T();
void F();

char peek() {
    return input[index]; // Current character
}

void match(char expected) {
    if (peek() == expected) {
        index++;
    } else {
        printf("Failure\n");
        exit(0);
    }
}

void E() {
    T();
    while (peek() == '+') {
        match('+');
        T();
    }
}

void T() {
    F();
    while (peek() == '*') {
        match('*');
        F();
    }
}

void F() {
    if (isalpha(peek())) { // id (single letter)
        match(peek());
    } else if (peek() == '(') {
        match('(');
        E();
        match(')');
    } else {
        printf("Failure\n");
        exit(0);
    }
}

int main() {
    char inputBuffer[100];
    printf("Enter an arithmetic expression: ");
    scanf("%s", inputBuffer);
    input = inputBuffer;

    E(); // Start parsing

    if (input[index] == '\0') {
        printf("Success\n");
    } else {
        printf("Failure\n");
    }

    return 0;
}
