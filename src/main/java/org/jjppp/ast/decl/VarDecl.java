package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.Type;

import java.util.Optional;

public record VarDecl(String name, Type type, Optional<Exp> defValExp, boolean isGlobal) implements Decl {
    public static VarDecl of(String name, Type type, Exp defValExp) {
        return new VarDecl(name, type, Optional.ofNullable(defValExp), SymTab.isGlobal());
    }

    public static VarDecl of(String name, Type type, Exp defValExp, boolean isGlobal) {
        return new VarDecl(name, type, Optional.ofNullable(defValExp), isGlobal);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public boolean isConst() {
        return type.isConst();
    }
}
