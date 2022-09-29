package org.jjppp.tools.parse;

import org.jjppp.ast.exp.FunExp;

import java.util.Optional;

public abstract class Lib {
    public static Optional<FunExp> convert(FunExp funExp) {
        for (var libFun : LibFun.class.getEnumConstants()) {
            if (libFun.name().equalsIgnoreCase(funExp.fun().name())) {
                return Optional.of(libFun.convert(funExp));
            }
        }
        return Optional.empty();
    }

    public enum LibFun {
        // INPUT
        GETINT {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        GETCH {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        GETARRAY {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        GETFLOAT {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        GETFARRAY {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },

        // OUTPUT
        PUTINT {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        PUTCH {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        PUTARRAY {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        PUTFLOAT {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        PUTFARRAY {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        },
        PUTF {
            @Override
            public FunExp convert(FunExp funExp) {
                return null;
            }
        };

        public FunExp convert(FunExp funExp) {
            return funExp;
        }
    }
}
