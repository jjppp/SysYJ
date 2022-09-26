package org.jjppp.ast.stmt;

import org.jjppp.ast.cond.Cond;

public record Ife(Cond cond, Stmt sTru, Stmt sFls) implements Stmt {
    public static Ife of(Cond cond, Stmt sTru, Stmt sFls) {
        return new Ife(cond, sTru, sFls);
    }
}
