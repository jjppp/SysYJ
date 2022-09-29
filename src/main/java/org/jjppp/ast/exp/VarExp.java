package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.runtime.Val;

public record VarExp(VarDecl var) implements LVal {
    public static VarExp of(VarDecl var) {
        return new VarExp(var);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        return var.defValExp().orElseThrow().constEval();
    }

    @Override
    public Decl getDecl() {
        return var;
    }
}
