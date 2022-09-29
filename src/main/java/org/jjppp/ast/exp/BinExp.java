package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Binary;
import org.jjppp.runtime.Val;

/**
 * Binary Exp
 */
public final class BinExp extends Binary<Exp> implements OpExp {
    private final BiOp op;

    private BinExp(Exp lhs, Exp rhs, BiOp op) {
        super(lhs, rhs);
        this.op = op;
    }

    public static BinExp of(BiOp op, Exp lhs, Exp rhs) {
        return new BinExp(lhs, rhs, op);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        return op.apply(getLhs().constEval(), getRhs().constEval());
    }

    @Override
    public Op getOp() {
        return op;
    }
}
