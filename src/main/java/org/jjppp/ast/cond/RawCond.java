package org.jjppp.ast.cond;

import org.jjppp.ast.Unary;
import org.jjppp.ast.exp.Exp;

public class RawCond extends Unary<Exp> implements Cond {
    private final Op op;

    private RawCond(Op op, Exp sub) {
        super(sub);
        this.op = op;
    }

    public static RawCond of(Op op, Exp sub) {
        return new RawCond(op, sub);
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
