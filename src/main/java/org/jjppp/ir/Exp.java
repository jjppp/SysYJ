package org.jjppp.ir;

import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.exp.OpExp;
import org.jjppp.type.IntType;
import org.jjppp.type.Type;

import java.util.List;

public interface Exp {
    Type type();

    record BiExp(OpExp.BiOp op, Ope lhs, Ope rhs) implements Exp {
        @Override
        public Type type() {
            return switch (op) {
                case ADD, SUB, DIV, MUL, MOD -> lhs.type();
                case EQ, NE, LE, GE, LT, GT, AND, OR -> IntType.ofConst();
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
                case NONE, NEG, POS -> sub.type();
                case NOT -> IntType.ofConst();
            };
        }

        @Override
        public String toString() {
            return switch (op) {
                case NONE -> sub.toString();
                default -> op + " " + sub;
            };
        }
    }

    record Call(FunDecl funDecl, List<Ope> args) implements Exp {
        @Override
        public Type type() {
            return funDecl.type().retType();
        }

        @Override
        public String toString() {
            return "call @" + funDecl.name() + " " + args;
        }
    }

    record Load(Var loc, Var off) implements Exp {
        @Override
        public Type type() {
            return loc.type();
        }

        @Override
        public String toString() {
            return loc + "[" + off + "]";
        }
    }
}
