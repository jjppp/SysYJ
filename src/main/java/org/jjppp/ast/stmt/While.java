package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;

public record While(Exp cond, Stmt body) implements Stmt {
    public static While of(Exp cond, Stmt body) {
        return new While(cond, body);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
