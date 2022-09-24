package org.jjppp.ast.cond;

import org.jjppp.ast.Binary;
import org.jjppp.ast.exp.Exp;

/**
 * Relational Conditions
 * lhs `cmp` rhs
 */
public final class RelCond extends Binary<Exp> implements Cond {
    private final Op op;

    public RelCond(Op op, Exp lhs, Exp rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    @Override
    public boolean isTrue() {
        return false;
    }

    private enum Op {
        LE, LT, GT, GE, NE, EQ
    }
}
