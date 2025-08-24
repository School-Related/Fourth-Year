%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.tab.h"  // Include the Bison header file
%}

%%
"if"      { return IF; }
"else"    { return ELSE; }
"while"   { return WHILE; }
"do"      { return DO; }
"{"       { return LBRACE; }
"}"       { return RBRACE; }
":"       { return COLON; }
";"       { return SEMICOLON; }
"("       { return LPAREN; }
")"       { return RPAREN; }
[a-zA-Z_][a-zA-Z0-9_]*  { return ID; }
[ \t\r\n]+  { /* Ignore whitespace */ }
.      {
	printf("Invalid character: %c\n", yytext[0]);
	return -1;
       }
%%

int yywrap() { return 1; }
