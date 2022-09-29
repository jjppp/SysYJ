package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.runtime.Val;

import java.util.List;

/**
 * Function call Exp
 */
public record FunExp(FunDecl fun, List<Exp> args) implements Exp {
    public static FunExp of(FunDecl fun, List<Exp> args) {
        return new FunExp(fun, args);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        throw new AssertionError("TODO");
    }
}
