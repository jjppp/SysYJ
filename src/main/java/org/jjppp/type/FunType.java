package org.jjppp.type;

import org.jjppp.ast.decl.Decl;

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

    @Override
    public boolean isConst() {
        return false;
    }
}
