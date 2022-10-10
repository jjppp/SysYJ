package org.jjppp.ast.exp;

import org.jjppp.runtime.Val;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public interface OpExp extends Exp {
    Op getOp();

    enum BiOp implements BinaryOperator<Val>, Op {
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

    enum UnOp implements UnaryOperator<Val>, Op {
        TOF, TOI,
        NEG, POS,
        NOT,
        NONE;

        @Override
        public Val apply(Val val) {
            return switch (this) {
                case NEG -> val.neg();
                case POS -> val;
                default -> throw new AssertionError("TODO");
            };
        }

        @Override
        public int prior() {
            return 13;
        }
    }

    interface Op {
        int prior();
    }
}
