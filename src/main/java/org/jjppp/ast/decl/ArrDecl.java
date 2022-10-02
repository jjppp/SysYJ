package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.type.ArrType;

public record ArrDecl(String name, ArrType type) implements Decl {
    public static ArrDecl of(String name, ArrType type) {
        return new ArrDecl(name, type);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ArrType type() {
        return type;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isConst() {
        return type.isConst();
    }
}
