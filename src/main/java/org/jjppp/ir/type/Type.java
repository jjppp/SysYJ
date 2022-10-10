package org.jjppp.ir.type;

import org.jjppp.type.ArrType;

public interface Type {
    static Type from(org.jjppp.type.Type type) {
        if (type instanceof org.jjppp.type.BaseType baseType) {
            return BaseType.from(baseType);
        } else if (type instanceof ArrType arrType) {
            return Loc.of(BaseType.from(arrType.type()));
        } else {
            throw new AssertionError("TODO");
        }
    }

    int size();
}
