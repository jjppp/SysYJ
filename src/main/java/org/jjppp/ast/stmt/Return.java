package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;

import java.util.Optional;

public record Return(Optional<Exp> exp) implements Stmt {
    public static Return of(Exp exp) {
        return new Return(Optional.ofNullable(exp));
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
