package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Unary;
import org.jjppp.runtime.Val;
import org.jjppp.type.FloatType;
import org.jjppp.type.IntType;
import org.jjppp.type.Type;

public class UnExp extends Unary<Exp> implements OpExp {
    private final UnOp op;

    private Type type = null;

    private UnExp(UnOp op, Exp sub) {
        super(sub);
        this.op = op;
    }

    public static UnExp of(UnOp op, Exp sub) {
        return new UnExp(op, sub);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        return op.apply(getSub().constEval());
    }

    @Override
    public UnOp getOp() {
        return op;
    }

    @Override
    public Type type() {
        if (type == null) {
            Type subType = getSub().type();
            type = switch (getOp()) {
                case TOI, NOT -> IntType.ofNonConst();
                case POS, NEG, NONE -> subType;
                case TOF -> FloatType.ofNonConst();
            };
        }
        return type;
    }
}
