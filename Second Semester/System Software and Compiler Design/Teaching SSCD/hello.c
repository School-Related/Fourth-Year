#include <stdio.h>
#define MAX 100

// function macro
#define AREA(x) (3.14*x*x)
int main()
{
    printf("Hello, world!\n");
    printf("Area of circle with radius 5: %f\n", AREA(5));
    return 0;
}
