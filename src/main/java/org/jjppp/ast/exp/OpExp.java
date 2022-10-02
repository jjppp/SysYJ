package org.jjppp.ast.exp;

import org.jjppp.runtime.Val;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public interface OpExp extends Exp {
    Op getOp();

    enum BiOp implements BinaryOperator<Val>, Op {
        ADD, SUB, MUL, DIV, MOD,
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
    }

    enum UnOp implements UnaryOperator<Val>, Op {
        NEG, POS,
        NOT,
        IZ, NZ;

        @Override
        public Val apply(Val val) {
            return switch (this) {
                case NEG -> val.neg();
                case POS -> val;
                default -> throw new AssertionError("TODO");
            };
        }
    }

    interface Op {
    }
}
