#include <stdio.h>

__declspec(dllexport) void sayHello()
{
    printf("Hello from DLL!\n");
}
