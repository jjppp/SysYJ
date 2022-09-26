grammar SysY;

@header {
package org.jjppp.parser;
}

compUnit : ( decl | funcDef )*;

decl : constDecl | varDecl;

constDecl : CONST bType constDef (',' constDef)* ';';

bType : INT | FLOAT;

constDef : ID ('[' exp ']')* '=' constInitVal;

constInitVal
    : exp
    | '{' (constInitVal (',' constInitVal)*)? '}'
    ;

varDecl : bType varDef (',' varDef)* ';';

varDef : ID ('[' exp ']')* ('=' initVal)?;

initVal
    : '{' (initVal (',' initVal)*)? '}'
    | exp
    ;

funcDef : funcType ID '(' funcFParams? ')' block;

funcType
    : VOID
    | INT
    | FLOAT
    ;

funcFParam : bType ID ('[' ']' ('[' exp ']')*)?;

funcFParams : funcFParam (',' funcFParam)*;

funcRParams : exp (',' exp)*;

block : '{' blockItem* '}';

blockItem : decl | stmt;

stmt
    : RETURN exp? ';'                                   #returnStmt
    | lVal '=' exp ';'                                  #assignStmt
    | CONTINUE ';'                                      #continStmt
    | BREAK ';'                                         #breakStmt
    | exp ';'                                           #expStmt
    | ';'                                               #emptyStmt
    | IF '(' cond ')' stmt                              #iftStmt
    | IF '(' cond ')' stmt ELSE stmt                    #ifteStmt
    | WHILE '(' cond ')' stmt                           #whileStmt
    | block                                             #blockStmt
    ;

lVal
    : ID
    | ID ('[' exp ']')+
    ;

number
    : DECIMAL_CONST
    | HEXADECIMAL_CONST
    | OCTAL_CONST
    | FLOAT_CONST
    ;

exp
    : exp (MUL | DIV | MOD) exp                         #mulExp
    | exp (ADD | SUB) exp                               #addExp
    | '(' exp ')'                                       #bracketExp
    | lVal                                              #lValExp
    | number                                            #unaryExp
    | ID '(' funcRParams? ')'                           #funExp
    | (ADD | SUB) exp                                   #unaryExp
    ;

cond
    : NOT? exp                                          #rawCond
    | exp (LE | LT | GE | GT) exp                       #relCond
    | exp (EQ | NE) exp                                 #eqCond
    | NOT cond                                          #unaryCond
    | cond (AND | OR) cond                              #binaryCond
    ;

/*****************************************************************/

fragment NONZERO_DIGIT      : [1-9];
fragment DECIMAL_DIGIT      : [0-9];
fragment OCTAL_DIGIT        : [0-7];
fragment HEXADECIMAL_DIGIT  : [0-9a-fA-F];

// keywords
INT                 : 'int';
FLOAT               : 'float';
VOID                : 'void';
IF                  : 'if';
ELSE                : 'else';
WHILE               : 'while';
BREAK               : 'break';
CONTINUE            : 'continue';
RETURN              : 'return';
CONST               : 'const';

// operators
ADD                 : '+';
SUB                 : '-';
MUL                 : '*';
DIV                 : '/';
MOD                 : '%';

NOT                 : '!';
AND                 : '&&';
OR                  : '||';
LE                  : '<=';
LT                  : '<';
GE                  : '>=';
GT                  : '>';
EQ                  : '==';
NE                  : '!=';

ID                  : [a-z_A-Z][a-z_A-Z0-9]*;
HEXADECIMAL_CONST   : '0x' HEXADECIMAL_DIGIT+;
DECIMAL_CONST       : NONZERO_DIGIT DECIMAL_DIGIT*;
OCTAL_CONST         : '0' | '0' OCTAL_DIGIT*;
FLOAT_CONST         : [0-9]*'.'[0-9]*;
WS                  : [\r\n\t ] -> skip;
COMMENT             : '//' ~('\n')* '\n' -> skip;
BLOCK_COMMENT       : '/*' .*? '*/' -> skip;