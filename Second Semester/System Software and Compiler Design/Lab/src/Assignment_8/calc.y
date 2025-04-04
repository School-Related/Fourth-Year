%{
#include <stdio.h>
#include <stdlib.h>

void yyerror(const char *s);
int yylex();
%}

%token NUMBER

%%
line:
    line expr '\n' { printf("Result = %d\n", $2); }
  | /* empty */
;

expr:
    expr '+' term { $$ = $1 + $3; }
  | expr '-' term { $$ = $1 - $3; }
  | term
;

term:
    term '*' factor { $$ = $1 * $3; }
  | term '/' factor {
        if ($3 == 0) {
            printf("Error: Division by zero\n");
            $$ = 0;
        } else {
            $$ = $1 / $3;
        }
    }
  | factor
;

factor:
    '(' expr ')' { $$ = $2; }
  | NUMBER
;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Syntax error: %s\n", s);
}

int main() {
    printf("Enter expressions (Ctrl+D to quit):\n");
    yyparse();
    return 0;
}
