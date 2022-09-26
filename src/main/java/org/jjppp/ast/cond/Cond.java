package org.jjppp.ast.cond;

/**
 * A Cond can only be used in `if` `while`
 * just another name for `Exp`s that eval
 * to bool (since we don't have bool type)
 */
public interface Cond {
    boolean isTrue();

    Op getOp();

    enum Op {
        LE, LT, GT, GE, NE, EQ,
        AND, OR, NOT,
        NZ, IZ
    }
}
