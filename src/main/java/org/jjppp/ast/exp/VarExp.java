package org.jjppp.ast.exp;

import org.jjppp.ast.Var;
import org.jjppp.runtime.Val;

public record VarExp(Var var) implements LVal {
    public static VarExp of(Var var) {
        return new VarExp(var);
    }

    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }
}
