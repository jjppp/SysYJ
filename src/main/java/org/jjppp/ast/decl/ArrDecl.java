package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.runtime.Val;
import org.jjppp.type.ArrType;

import java.util.Optional;

public record ArrDecl(String name, ArrType type, Optional<Val> defVal) implements Decl {
    public static ArrDecl of(String name, ArrType type, Val defVal) {
        return new ArrDecl(name, type, Optional.ofNullable(defVal));
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
