package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.runtime.Val;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.Type;

public record VarExp(VarDecl var) implements LVal {
    public static VarExp of(VarDecl var) {
        return new VarExp(var);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isConst() {
        return var.isConst();
    }

    @Override
    public Val constEval() {
        return SymTab.getInstance().getVal(var.name());
    }

    @Override
    public Decl getDecl() {
        return var;
    }

    @Override
    public Type type() {
        return var().type();
    }
}
