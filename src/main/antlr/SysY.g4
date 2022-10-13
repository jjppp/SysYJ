grammar SysY;

@header {
package org.jjppp.parser;
}

compUnit : decl*;

decl
    : CONST bType def (',' def)* ';'                    #constDecl
    | bType def (',' def)* ';'                          #varDecl
    | funcType ID '(' funcFParams? ')' scope            #funcDecl
    ;

bType : INT | FLOAT;

def : ID ('[' exp ']')* ('=' initVal)?;

initVal
    : '{' (initVal (',' initVal)*)? '}'                 #arrInitVal
    | exp                                               #expInitVal
    ;

funcType
    : VOID
    | INT
    | FLOAT
    ;

funcFParam
    : bType ID                                          #varFParam
    | bType ID '[' ']' ('[' exp ']')*                   #arrFParam
    ;

funcFParams : funcFParam (',' funcFParam)*;

funcRParams : exp (',' exp)*;

scope : '{' blockItem* '}';

blockItem
    : decl                                              #declItem
    | stmt                                              #stmtItem
    ;

stmt
    : RETURN exp? ';'                                   #returnStmt
    | lVal '=' exp ';'                                  #assignStmt
    | CONTINUE ';'                                      #continStmt
    | BREAK ';'                                         #breakStmt
    | exp ';'                                           #expStmt
    | ';'                                               #emptyStmt
    | IF '(' exp  ')' stmt                              #iftStmt
    | IF '(' exp  ')' sTru=stmt ELSE sFls=stmt          #ifteStmt
    | WHILE '(' exp  ')' stmt                           #whileStmt
    | scope                                             #blockStmt
    ;

lVal
    : ID                                                #idLVal
    | ID ('[' exp ']')+                                 #arrLVal
    ;

number
    : DECIMAL_CONST                                     #decNum
    | HEXADECIMAL_CONST                                 #hexNum
    | OCTAL_CONST                                       #octNum
    | FLOAT_CONST                                       #fltNum
    ;

exp
    : lhs=exp op=(MUL | DIV | MOD) rhs=exp              #mulExp
    | lhs=exp op=(ADD | SUB) rhs=exp                    #addExp
    | '(' exp ')'                                       #bracketExp
    | lVal                                              #lValExp
    | number                                            #valExp
    | fun=ID '(' funcRParams? ')'                       #funExp
    | op=(ADD | SUB | NOT) exp                          #unaryExp
    | lhs=exp op=(LE | LT | GE | GT) rhs=exp            #relCond
    | lhs=exp op=(EQ | NE) rhs=exp                      #eqCond
    | lhs=exp op=(AND | OR) rhs=exp                     #binaryCond
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
