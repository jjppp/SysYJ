grammar SysY;

@header {
package org.jjppp.parser;
}

compUnit : ( decl | funcDef )*;

decl : constDecl | varDecl;

constDecl : 'const' bType constDef (',' constDef) ';';

bType : 'int' | 'float';

constDef : ID ('[' constExp ']') '=' constInitVal;

constInitVal
    : constExp
    | '{' '}'
    | '{' constInitVal (',' constInitVal) '}'
    ;

varDecl : bType varDef (',' varDef) ';';

varDef
    : ID ('[' constExp ']')*
    | ID ('[' constExp ']')* '=' initVal
    ;

initVal
    : '{' initVal (',' initVal)* '}'
    | '{' '}'
    | exp
    ;

funcDef
    : funcType ID '(' ')' block
    | funcType ID '(' funcFParam (',' funcFParam)* ')' block
    ;

funcType : 'void' | 'int' | 'float';

funcFParam
    : bType ID
    | bType ID '[' ']' ('[' exp ']')*;

block : '{' blockItem* '}';

blockItem : decl | stmt;

stmt
    : lVal '=' exp ';'
    | ';'
    | exp ';'
    | block
    | 'if' '(' cond ')' stmt
    | 'if' '(' cond ')' stmt 'else' stmt
    | 'while' '(' cond ')' stmt
    | 'break' ';'
    | 'continue' ';'
    | 'return' ?exp ';'
    ;

exp : addExp;

cond : lOrExp;

lVal
    : ID
    | ID ('[' exp ']')+
    ;

primaryExp
    : '(' exp ')'
    | lVal
    | number
    ;

number
    : DECIMAL_CONST
    | HEXADECIMAL_CONST
    | OCTAL_CONST
    | FLOAT_CONST
    ;

unaryExp
    : primaryExp
    | ID '(' funcRParams ')'
    | ('+' | '-' | '!') unaryExp
    ;

funcRParams
    : exp (',' exp)*
    |
    ;

mulExp : unaryExp | mulExp ('*' | '/' | '%') unaryExp;

addExp : mulExp | addExp ('+' | '−') mulExp;

relExp : addExp | relExp ('<' | '>' | '<=' | '>=') addExp;

eqExp : relExp | eqExp ('==' | '!=') relExp;

lAndExp : eqExp | lAndExp '&&' eqExp;

lOrExp : lAndExp | lOrExp '||' lAndExp;

constExp: addExp;

/*****************************************************************/

fragment NONZERO_DIGIT      : [1-9];
fragment DECIMAL_DIGIT      : [0-9];
fragment OCTAL_DIGIT        : [0-7];
fragment HEXADECIMAL_DIGIT  : [0-9a-fA-F];

FUNC_TYPE
    : 'void'
    | 'float'
    | 'int'
    ;

ID                  : [a-z_A-Z][a-z_A-Z0-9]*;
HEXADECIMAL_CONST   : '0x' HEXADECIMAL_DIGIT+;
DECIMAL_CONST       : NONZERO_DIGIT DECIMAL_DIGIT*;
OCTAL_CONST         : '0' | '0' OCTAL_DIGIT*;
FLOAT_CONST         : [0-9]*'.'[0-9]*;
WS                  : [\r\n\t ] -> skip;