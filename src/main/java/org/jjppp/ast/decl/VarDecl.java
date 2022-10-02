package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.type.Type;

public record VarDecl(String name, Type type) implements Decl {
    public static VarDecl of(String name, Type type) {
        return new VarDecl(name, type);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public boolean isConst() {
        return type.isConst();
    }
}
