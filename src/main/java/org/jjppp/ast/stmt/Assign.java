package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.LVal;

public record Assign(LVal lhs, Exp rhs) implements Stmt {
    public static Assign of(LVal lhs, Exp rhs) {
        return new Assign(lhs, rhs);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
