package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;

public record If(Exp cond, Stmt sTru) implements Stmt {
    public static If of(Exp cond, Stmt sTru) {
        return new If(cond, sTru);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
