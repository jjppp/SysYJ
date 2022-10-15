package org.jjppp.ast.exp.op;

import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Val;

import java.util.function.BinaryOperator;

public enum BiOp implements BinaryOperator<BaseVal>, Op {
    ADD, SUB, MUL, DIV, MOD,
    FADD, FSUB, FMUL, FDIV,
    PADD,

    LE, LT, GE, GT, NE, EQ,
    AND, OR;

    @Override
    public BaseVal apply(BaseVal lhs, BaseVal rhs) {
        String name = this.toString().toLowerCase();
        try {
            return (BaseVal) lhs.getClass()
                    .getMethod(name, Val.class)
                    .invoke(lhs, rhs);
        } catch (Exception e) {
            System.out.println(name);
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
