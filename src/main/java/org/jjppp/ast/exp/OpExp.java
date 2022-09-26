package org.jjppp.ast.exp;

public interface OpExp extends Exp {
    Op getOp();

    enum Op {
        ADD, SUB, MUL, DIV, MOD,
        NEG, POS
    }
}
