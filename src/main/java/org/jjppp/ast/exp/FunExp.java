package org.jjppp.ast.exp;

import org.jjppp.ast.Fun;
import org.jjppp.runtime.Val;

import java.util.List;

/**
 * Function call Exp
 */
public record FunExp(Fun fun, List<Exp> args) implements Exp {
    public static FunExp of(Fun fun, List<Exp> args) {
        return new FunExp(fun, args);
    }

    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }
}
