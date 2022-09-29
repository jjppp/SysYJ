package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;

public record ExpStmt(Exp exp) implements Stmt {
    public static ExpStmt of(Exp exp) {
        return new ExpStmt(exp);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
