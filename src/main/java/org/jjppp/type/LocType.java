package org.jjppp.type;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.runtime.BaseVal;

public record LocType(BaseType baseType) implements Type {
    public static LocType of(BaseType baseType) {
        return new LocType(baseType);
    }

    public static LocType from(ArrDecl arrDecl) {
        return new LocType(arrDecl.type().type());
    }

    @Override
    public String toString() {
        return "*" + baseType;
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public BaseVal defVal() {
        throw new AssertionError("TODO");
    }
}
