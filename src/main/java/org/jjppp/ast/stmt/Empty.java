package org.jjppp.ast.stmt;

public final class Empty implements Stmt {
    private final static Empty INSTANCE = new Empty();

    private Empty() {
    }


    public static Empty getInstance() {
        return INSTANCE;
    }
}
