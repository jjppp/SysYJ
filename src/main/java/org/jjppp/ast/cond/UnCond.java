package org.jjppp.ast.cond;

import org.jjppp.ast.Unary;

public final class UnCond extends Unary<Cond> implements Cond {
    private final Op op;

    private UnCond(Op op, Cond sub) {
        super(sub);
        this.op = op;
    }

    public static UnCond of(Op op, Cond sub) {
        return new UnCond(op, sub);
    }

    @Override
    public boolean isTrue() {
        throw new AssertionError("TODO");
    }

    @Override
    public Op getOp() {
        return op;
    }
}
