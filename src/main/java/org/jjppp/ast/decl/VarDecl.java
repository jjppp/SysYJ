package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.BaseType;

import java.util.Optional;

public record VarDecl(String name, BaseType type, Optional<Exp> defValExp, boolean isGlobal, int id) implements Decl {
    private static int VARDECL_COUNT = 0;

    public static VarDecl of(String name, BaseType type, Exp defValExp) {
        VARDECL_COUNT += 1;
        return new VarDecl(name, type, Optional.ofNullable(defValExp), SymTab.getInstance().isGlobal(), VARDECL_COUNT);
    }

    public static VarDecl of(String name, BaseType type, Exp defValExp, boolean isGlobal) {
        VARDECL_COUNT += 1;
        return new VarDecl(name, type, Optional.ofNullable(defValExp), isGlobal, VARDECL_COUNT);
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
