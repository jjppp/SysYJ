package org.jjppp.ast.exp;

import org.jjppp.ast.decl.FunDecl;
import org.jjppp.runtime.Val;

import java.util.List;

/**
 * Function call Exp
 */
public record FunExp(FunDecl funDef, List<Exp> args) implements Exp {
    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }
}
