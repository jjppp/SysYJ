package org.jjppp.ast.stmt;

import org.jjppp.ast.cond.Cond;

public record If(Cond cond, Stmt sTru) implements Stmt {
    public static If of(Cond cond, Stmt sTru) {
        return new If(cond, sTru);
    }
}
