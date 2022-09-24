package org.jjppp.ast.decl;

import org.jjppp.type.Type;

public record ArrDecl(String name, Type type, boolean isConst) implements Decl {
}
