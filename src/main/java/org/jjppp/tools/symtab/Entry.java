package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.Decl;
import org.jjppp.type.Type;

public interface Entry {
    default String getName() {
        return getDecl().name();
    }

    Decl getDecl();

    boolean isGlobal();

    default Type getType() {
        return getDecl().type();
    }
}
