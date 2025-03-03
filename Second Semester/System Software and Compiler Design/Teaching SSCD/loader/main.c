#include <windows.h>
#include <stdio.h>

typedef void (*HelloFunc)();

int main()
{
    HINSTANCE hLib = LoadLibrary("mylib.dll");
    if (!hLib)
    {
        printf("Failed to load DLL\n");
        return 1;
    }

    HelloFunc sayHello = (HelloFunc)GetProcAddress(hLib, "sayHello");
    if (!sayHello)
    {
        printf("Failed to get function address\n");
        return 1;
    }

    sayHello();
    FreeLibrary(hLib);
    return 0;
}
// gcc -shared -o mylib.dll - DBUILD_DLL mylib.c
// gcc -o myprogram.exe main.c -L. -lmylib
// myprogram.exe
