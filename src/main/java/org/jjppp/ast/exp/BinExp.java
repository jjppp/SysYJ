package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Binary;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.runtime.Int;
import org.jjppp.runtime.Val;
import org.jjppp.type.IntType;
import org.jjppp.type.Type;

/**
 * Binary Exp
 */
public final class BinExp extends Binary<Exp> implements OpExp {
    private final BiOp op;

    private Type type = null;

    private BinExp(Exp lhs, Exp rhs, BiOp op) {
        super(lhs, rhs);
        this.op = op;
    }

    public static BinExp of(BiOp op, Exp lhs, Exp rhs) {
        return new BinExp(lhs, rhs, op);
    }

    public static BinExp of(BiOp op, VarDecl lhs, VarDecl rhs) {
        return new BinExp(VarExp.of(lhs), VarExp.of(rhs), op);
    }

    public static BinExp of(BiOp op, VarDecl lhs, Exp rhs) {
        return new BinExp(VarExp.of(lhs), rhs, op);
    }

    public static BinExp of(BiOp op, Exp lhs, VarDecl rhs) {
        return new BinExp(lhs, VarExp.of(rhs), op);
    }

    public static BinExp of(BiOp op, VarDecl lhs, int rhs) {
        return new BinExp(VarExp.of(lhs), ValExp.of(Int.from(rhs)), op);
    }

    public static BinExp of(BiOp op, Exp lhs, int rhs) {
        return new BinExp(lhs, ValExp.of(Int.from(rhs)), op);
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
    public BiOp getOp() {
        return op;
    }

    @Override
    public Type type() {
        if (type == null) {
            Type lhsType = getLhs().type();
            Type rhsType = getRhs().type();
            type = switch (getOp()) {
                case ADD, SUB, MUL, DIV, MOD,
                        FADD, FSUB, FMUL, FDIV,
                        PADD -> lhsType;
                case EQ, GE, NE, LE, GT, LT, AND, OR -> IntType.ofNonConst();
            };
        }
        return type;
    }
}
