package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.cond.Cond;

public record While(Cond cond, Stmt body) implements Stmt {
    public static While of(Cond cond, Stmt body) {
        return new While(cond, body);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
