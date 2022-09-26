package org.jjppp.ast.decl;

import org.jjppp.ast.exp.Exp;
import org.jjppp.type.Type;

import java.util.Optional;

public record VarDecl(String name, Type type, Optional<Exp> defVal) implements Decl {
    public static VarDecl of(String name, Type type, Exp defVal) {
        return new VarDecl(name, type, Optional.ofNullable(defVal));
    }
}
