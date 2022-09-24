package org.jjppp.ast.exp;

import org.jjppp.ast.Var;
import org.jjppp.runtime.Val;

public record VarExp(Var var) implements LVal {
    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }
}
