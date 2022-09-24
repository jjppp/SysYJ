package org.jjppp.ast.exp;

import org.jjppp.ast.Arr;
import org.jjppp.runtime.Val;

import java.util.List;

public record ArrExp(Arr arr, List<Exp> indices) implements LVal {
    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }
}
