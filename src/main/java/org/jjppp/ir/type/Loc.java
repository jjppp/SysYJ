package org.jjppp.ir.type;

import org.jjppp.ast.decl.ArrDecl;

public record Loc(BaseType baseType) implements Type {
    public static Loc of(BaseType baseType) {
        return new Loc(baseType);
    }

    public static Loc from(ArrDecl arrDecl) {
        return new Loc(BaseType.from(arrDecl.type().type()));
    }

    @Override
    public String toString() {
        return baseType + "*";
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("pointer types are machine dependent");
    }
}
