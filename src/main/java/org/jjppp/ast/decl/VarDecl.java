package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;
import org.jjppp.type.Type;

import java.util.Optional;

public record VarDecl(String name, Type type, Optional<Exp> defValExp) implements Decl {
    public static VarDecl of(String name, Type type, Exp defValExp) {
        return new VarDecl(name, type, Optional.ofNullable(defValExp));
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
