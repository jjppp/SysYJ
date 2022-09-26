package org.jjppp.ast;

import org.jjppp.ast.decl.FunDecl;

import java.util.Optional;

public final class Fun {
    private final String name;

    private FunDecl decl;

    private Fun(String name) {
        this.name = name;
    }

    public static Fun of(String name) {
        return new Fun(name);
    }

    public String getName() {
        return name;
    }

    public Optional<FunDecl> getDecl() {
        return Optional.ofNullable(decl);
    }

    public void setDecl(FunDecl decl) {
        this.decl = decl;
    }
}
