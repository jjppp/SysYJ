package org.jjppp.ast.exp;

import org.jjppp.runtime.Val;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public interface OpExp extends Exp {
    Op getOp();

    enum BiOp implements BinaryOperator<Val>, Op {
        ADD, SUB, MUL, DIV, MOD;

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
        NEG, POS;

        @Override
        public Val apply(Val val) {
            return switch (this) {
                case NEG -> val.neg();
                case POS -> val;
            };
        }
    }

    interface Op {
    }
}
