package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.BaseType;

import java.util.Optional;

public record VarDecl(String name, BaseType type, Optional<Exp> defValExp, boolean isGlobal) implements Decl {
    public static VarDecl of(String name, BaseType type, Exp defValExp) {
        return new VarDecl(name, type, Optional.ofNullable(defValExp), SymTab.getInstance().isGlobal());
    }

    public static VarDecl of(String name, BaseType type, Exp defValExp, boolean isGlobal) {
        return new VarDecl(name, type, Optional.ofNullable(defValExp), isGlobal);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isConst() {
        return type.isConst();
    }
}
