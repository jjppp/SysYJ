package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;

public final class Empty implements Stmt {
    private final static Empty INSTANCE = new Empty();

    private Empty() {
    }


    public static Empty getInstance() {
        return INSTANCE;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
