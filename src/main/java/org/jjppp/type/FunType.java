package org.jjppp.type;

import org.jjppp.ast.decl.Decl;
import org.jjppp.runtime.BaseVal;

import java.util.List;
import java.util.stream.Collectors;

public record FunType(BaseType retType, List<Type> argTypes) implements Type {
    public static FunType from(BaseType retType, List<Decl> params) {
        return new FunType(
                retType,
                params.stream()
                        .map(Decl::type)
                        .collect(Collectors.toList()));
    }

    public static FunType of(BaseType retType, List<Type> argTypes) {
        return new FunType(retType, argTypes);
    }

    @Override
    public String toString() {
        return argTypes + " -> " + retType;
    }

    @Override
    public int size() {
        throw new AssertionError("Fun size()");
    }

    @Override
    public boolean isConst() {
        return false;
    }

    @Override
    public BaseVal defVal() {
        throw new UnsupportedOperationException("funType defVal()");
    }
}
