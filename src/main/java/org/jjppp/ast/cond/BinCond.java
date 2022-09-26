package org.jjppp.ast.cond;

import org.jjppp.ast.Binary;

/**
 * Binary Conditions
 * lhs && rhs
 * lhs || rhs
 */
public final class BinCond extends Binary<Cond> implements Cond {
    private final Op op;

    private BinCond(Op op, Cond lhs, Cond rhs) {
        super(lhs, rhs);
        this.op = op;
    }

    public static BinCond of(Op op, Cond lhs, Cond rhs) {
        return new BinCond(op, lhs, rhs);
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
