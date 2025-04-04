%{
#include <stdio.h>
#include <stdlib.h>
#include "calc.tab.h"
%}

%%
[0-9]+      { yylval = atoi(yytext); return NUMBER; }
[+\-*/()]   { return *yytext; }
\n          { return '\n'; }
[ \t]       ; // ignore whitespace
.           { printf("Invalid character: %s\n", yytext); }
%%

int yywrap() {
    return 1;
}
