package org.jjppp.ast.stmt;

public final class Continue implements Stmt {
    private final static Continue INSTANCE = new Continue();

    private Continue() {
    }

    public static Continue getInstance() {
        return INSTANCE;
    }
}
