package org.jjppp.ast.decl;

import org.jjppp.ast.Item;
import org.jjppp.type.Type;

public interface Decl extends Item {
    String name();

    Type type();

    default int size() {
        return type().size();
    }

    boolean isConst();
}
