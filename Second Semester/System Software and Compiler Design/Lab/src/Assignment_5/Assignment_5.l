%{
#include <stdio.h>
%}

%option noyywrap

%%
"int"|"float"|"return"|"if"|"else"|"while"  { printf("Keyword: %s\n", yytext); }
"+"|"-"|"*"|"/"|"="                          { printf("Operator: %s\n", yytext); }
"("|")"|"{"|"}"|";"|","            { printf("Special Symbol: %s\n", yytext); }
[0-9]+\.[0-9]+                               { printf("Float Number: %s\n", yytext); }
[0-9]+                                       { printf("Number: %s\n", yytext); }
[a-zA-Z_][a-zA-Z0-9_]*                        { printf("Identifier: %s\n", yytext); }
[ \t\n]+                                     { /* Ignore whitespace */ }
%%

int main() {
    yylex();
    return 0;
}
