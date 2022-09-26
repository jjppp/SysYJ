package org.jjppp.ast;

import org.jjppp.ast.decl.VarDecl;

import java.util.Optional;

public final class Var {
    private final String name;

    private VarDecl decl;

    private Var(String name) {
        this.name = name;
    }

    public static Var of(String name) {
        return new Var(name);
    }

    public String getName() {
        return name;
    }

    public Optional<VarDecl> getDecl() {
        return Optional.ofNullable(decl);
    }

    public void setDecl(VarDecl decl) {
        this.decl = decl;
    }
}
