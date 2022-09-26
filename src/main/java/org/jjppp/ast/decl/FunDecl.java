package org.jjppp.ast.decl;

import org.jjppp.ast.stmt.Block;
import org.jjppp.type.Type;

public record FunDecl(String name, Type type, Block body) implements Decl {
    public static FunDecl of(String name, Type type, Block body) {
        return new FunDecl(name, type, body);
    }
}
