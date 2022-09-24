package org.jjppp.ast.stmt;

public final class Break implements Stmt {
    private final static Break INSTANCE = new Break();

    private Break() {
    }

    public Break getInstance() {
        return INSTANCE;
    }
}
