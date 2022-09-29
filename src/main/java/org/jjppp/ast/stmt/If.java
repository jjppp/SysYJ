package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.cond.Cond;

public record If(Cond cond, Stmt sTru) implements Stmt {
    public static If of(Cond cond, Stmt sTru) {
        return new If(cond, sTru);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
