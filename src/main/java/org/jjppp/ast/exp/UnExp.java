package org.jjppp.ast.exp;

import org.jjppp.ast.Unary;
import org.jjppp.runtime.Val;

public class UnExp extends Unary<Exp> implements OpExp {
    private final Op op;

    private UnExp(Op op, Exp sub) {
        super(sub);
        this.op = op;
    }

    public static UnExp of(Op op, Exp sub) {
        return new UnExp(op, sub);
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
