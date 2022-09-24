package org.jjppp.ast.decl;

import org.jjppp.type.Type;

public record FunDecl(String name, Type type) implements Decl {
}
