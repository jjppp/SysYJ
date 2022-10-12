package org.jjppp.ast.exp.op;

import org.jjppp.runtime.Val;

import java.util.function.UnaryOperator;

public enum UnOp implements UnaryOperator<Val>, Op {
    TOF, TOI,
    NEG, POS,
    NOT;

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
