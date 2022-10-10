package org.jjppp.ir.type;

import org.jjppp.type.FloatType;
import org.jjppp.type.IntType;
import org.jjppp.type.VoidType;

public interface BaseType extends Type {
    static BaseType from(org.jjppp.type.BaseType type) {
        if (type instanceof IntType) {
            return BaseType.Int.Type();
        } else if (type instanceof FloatType) {
            return BaseType.Float.Type();
        } else if (type instanceof VoidType) {
            return BaseType.Void.Type();
        }
        System.out.println(type);
        throw new AssertionError("should not reach here");
    }

    final class Int implements BaseType {
        private final static Int INSTANCE = new Int();

        private Int() {
        }

        public static Int Type() {
            return INSTANCE;
        }

        @Override
        public String toString() {
            return "int";
        }

        @Override
        public int size() {
            return 4;
        }
    }

    final class Float implements BaseType {
        private final static Float INSTANCE = new Float();

        private Float() {
        }

        public static Float Type() {
            return INSTANCE;
        }

        @Override
        public String toString() {
            return "flt";
        }

        @Override
        public int size() {
            return 4;
        }
    }

    final class Void implements BaseType {
        private final static Void INSTANCE = new Void();

        private Void() {
        }

        public static Void Type() {
            return INSTANCE;
        }

        @Override
        public String toString() {
            return "void";
        }

        @Override
        public int size() {
            return 4;
        }
    }
}
