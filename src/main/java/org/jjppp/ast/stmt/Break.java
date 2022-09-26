package org.jjppp.ast.stmt;

public final class Break implements Stmt {
    private final static Break INSTANCE = new Break();

    private Break() {
    }

    public static Break getInstance() {
        return INSTANCE;
    }
}
