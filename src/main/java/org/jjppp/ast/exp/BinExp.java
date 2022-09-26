package org.jjppp.ast.exp;

import org.jjppp.ast.Binary;
import org.jjppp.runtime.Val;

/**
 * Binary Exp
 */
public final class BinExp extends Binary<Exp> implements OpExp {
    private final Op op;

    private BinExp(Exp lhs, Exp rhs, Op op) {
        super(lhs, rhs);
        this.op = op;
    }

    public static BinExp of(Op op, Exp lhs, Exp rhs) {
        return new BinExp(lhs, rhs, op);
    }

    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }

    @Override
    public Op getOp() {
        return op;
    }
}
