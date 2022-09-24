package org.jjppp.ast.decl;

import org.jjppp.type.Type;

public record VarDecl(boolean isConst, Type type) implements Decl {
}
