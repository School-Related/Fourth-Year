%{
#include <stdio.h>
#include <stdlib.h>
void yyerror(const char *s);
int yylex();
%}

%token IF ELSE WHILE DO ID COLON LBRACE RBRACE SEMICOLON LPAREN RPAREN

%right ELSE  // Resolves shift/reduce conflict

%%

stmt:
    compound_stmt
  | IF LPAREN ID RPAREN stmt %prec IF
  | IF LPAREN ID RPAREN stmt ELSE stmt
  | WHILE LPAREN ID RPAREN stmt
  | DO stmt WHILE LPAREN ID RPAREN SEMICOLON
  | ID COLON stmt
  | ID SEMICOLON  //  Allows statements like `x;`
  ;

compound_stmt:
    LBRACE stmt_list RBRACE
    ;

stmt_list:
    stmt
  | stmt_list stmt
  | /* empty */
    ;

%%

void yyerror(const char *s) {
    printf("Syntax error: %s\n", s);
}

int main() {
    printf("Enter a compound statement (Ctrl+D to stop):\n");
    if (yyparse() == 0) {
        printf("Parsing successful!\n");
    } else {
        printf("Parsing failed due to syntax errors.\n");
    }
    return 0;
}

