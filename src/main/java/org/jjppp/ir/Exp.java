package org.jjppp.ir;

import org.jjppp.ast.exp.OpExp;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Type;

import java.util.List;

public interface Exp {
    Type type();

    record BiExp(OpExp.BiOp op, Ope lhs, Ope rhs) implements Exp {
        @Override
        public Type type() {
            return switch (op) {
                case ADD, SUB, DIV, MUL, MOD,
                        FADD, FSUB, FDIV, FMUL,
                        PADD, PSUB, PMUL -> lhs.type();
                case EQ, NE, LE, GE, LT, GT, AND, OR -> BaseType.Int.Type();
            };
        }

        @Override
        public String toString() {
            return op + " " + lhs + " " + rhs;
        }
    }

    record UnExp(OpExp.UnOp op, Ope sub) implements Exp {
        @Override
        public Type type() {
            return switch (op) {
                case TOI, NOT -> BaseType.Int.Type();
                case TOF -> BaseType.Float.Type();
                case NONE, NEG, POS -> sub.type();
            };
        }

        @Override
        public String toString() {
            if (op.equals(OpExp.UnOp.NONE)) {
                return sub.toString();
            } else {
                return op + " " + sub;
            }
        }
    }

    record Call(Fun fun, List<Ope> args) implements Exp {
        @Override
        public Type type() {
            return fun.retType();
        }

        @Override
        public String toString() {
            return "call @" + fun.name() + " " + args;
        }
    }

    record Load(Var loc) implements Exp {
        @Override
        public Type type() {
            return loc.type();
        }

        @Override
        public String toString() {
            return "*" + loc;
        }
    }
}
