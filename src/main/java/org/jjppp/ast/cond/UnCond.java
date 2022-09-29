package org.jjppp.ast.cond;

import org.jjppp.ast.ASTVisitor;
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
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
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
