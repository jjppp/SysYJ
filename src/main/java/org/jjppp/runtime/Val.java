package org.jjppp.runtime;

import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Type;

public interface Val extends Comparable<Val> {
    Int toInt();

    Float toFloat();

    default Val add(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val sub(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val mul(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val div(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val mod(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val le(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val lt(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val ge(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val gt(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val eq(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val ne(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val and(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val or(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val neg() {
        throw new UnsupportedOperationException("");
    }

    default Val not() {
        throw new UnsupportedOperationException("");
    }

    final class Void implements BaseVal {
        private final static Void INSTANCE = new Void();

        private Void() {
        }

        public static Void getInstance() {
            return INSTANCE;
        }

        @Override
        public String toString() {
            return "void";
        }

        @Override
        public Type type() {
            return BaseType.Void.Type();
        }

        @Override
        public Int toInt() {
            throw new UnsupportedOperationException("void toInt()");
        }

        @Override
        public Float toFloat() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(Val val) {
            throw new UnsupportedOperationException("void compare()");
        }
    }
}
