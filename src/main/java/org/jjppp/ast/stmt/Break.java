package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;

public final class Break implements Stmt {
    private final static Break INSTANCE = new Break();

    private Break() {
    }

    public static Break getInstance() {
        return INSTANCE;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
