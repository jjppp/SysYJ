package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;

public final class Continue implements Stmt {
    private final static Continue INSTANCE = new Continue();

    private Continue() {
    }

    public static Continue getInstance() {
        return INSTANCE;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
