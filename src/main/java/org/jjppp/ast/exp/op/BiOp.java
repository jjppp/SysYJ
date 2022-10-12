package org.jjppp.ast.exp.op;

import org.jjppp.runtime.Val;

import java.util.function.BinaryOperator;

public enum BiOp implements BinaryOperator<Val>, Op {
    ADD, SUB, MUL, DIV, MOD,
    FADD, FSUB, FMUL, FDIV,
    PADD, PSUB, PMUL,

    LE, LT, GE, GT, NE, EQ,
    AND, OR;

    @Override
    public Val apply(Val lhs, Val rhs) {
        String name = this.toString().toLowerCase();
        try {
            return (Val) lhs.getClass()
                    .getMethod(name, Val.class)
                    .invoke(lhs, rhs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int prior() {
        return switch (this) {
            case MUL, MOD, DIV -> 12;
            case ADD, SUB -> 11;
            case LE, LT, GE, GT -> 10;
            case NE, EQ -> 9;
            case AND -> 8;
            case OR -> 7;
            default -> throw new AssertionError("TODO");
        };
    }
}
