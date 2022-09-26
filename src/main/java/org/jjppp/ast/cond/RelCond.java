package org.jjppp.ast.cond;

import org.jjppp.ast.Binary;
import org.jjppp.ast.exp.Exp;

/**
 * Relational Conditions
 * lhs `cmp` rhs
 */
public final class RelCond extends Binary<Exp> implements Cond {
    private final Op op;

    private RelCond(Op op, Exp lhs, Exp rhs) {
        super(lhs, rhs);
        this.op = op;
    }

    public static RelCond of(Op op, Exp lhs, Exp rhs) {
        return new RelCond(op, lhs, rhs);
    }

    @Override
    public Op getOp() {
        return op;
    }

    @Override
    public boolean isTrue() {
        throw new AssertionError("TODO");
    }
}
