package org.jjppp.ir;

import org.jjppp.ast.exp.OpExp;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Type;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Exp {
    Type type();

    Set<Var> useSet();

    default boolean isMove() {
        return false;
    }

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
        public Set<Var> useSet() {
            return Stream.of(lhs, rhs)
                    .filter(Var.class::isInstance)
                    .map(Var.class::cast)
                    .collect(Collectors.toSet());
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
        public Set<Var> useSet() {
            return Stream.of(sub)
                    .filter(Var.class::isInstance)
                    .map(Var.class::cast)
                    .collect(Collectors.toSet());
        }

        @Override
        public boolean isMove() {
            return op().equals(OpExp.UnOp.NONE);
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
        public Set<Var> useSet() {
            return args().stream()
                    .filter(Var.class::isInstance)
                    .map(Var.class::cast)
                    .collect(Collectors.toSet());
        }

        @Override
        public String toString() {
            return "call @" + fun.name() + " " + args;
        }
    }

    record Load(Ope loc) implements Exp {
        @Override
        public Type type() {
            return loc.type();
        }

        @Override
        public Set<Var> useSet() {
            if (loc instanceof Var var) {
                return Set.of(var);
            }
            return Collections.emptySet();
        }

        @Override
        public String toString() {
            return "*" + loc;
        }
    }
}
