#include "myheader.h"

int main()
{
    myFunction(); // Calls the function defined in myfunctions.c
    return 0;
}

// gcc -c myfunctions.c -o myfunctions.o
// gcc -c main.c -o main.o
// gcc main.o myfunctions.o -o myprogram.exe
// ./myprogram.exe
// Hello from myFunction!