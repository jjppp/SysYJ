package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;

public record Ife(Exp cond, Stmt sTru, Stmt sFls) implements Stmt {
    public static Ife of(Exp cond, Stmt sTru, Stmt sFls) {
        return new Ife(cond, sTru, sFls);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
