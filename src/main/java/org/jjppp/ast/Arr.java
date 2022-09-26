package org.jjppp.ast;

import org.jjppp.ast.decl.ArrDecl;

import java.util.Optional;

public final class Arr {
    private final String name;

    private ArrDecl decl;

    private Arr(String name) {
        this.name = name;
    }

    public static Arr of(String name) {
        return new Arr(name);
    }

    public String getName() {
        return name;
    }

    public Optional<ArrDecl> getDecl() {
        return Optional.ofNullable(decl);
    }

    public void setDecl(ArrDecl decl) {
        this.decl = decl;
    }
}
