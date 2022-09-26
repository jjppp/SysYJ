package org.jjppp.ast.stmt;

import org.jjppp.ast.exp.Exp;

public record ExpStmt(Exp exp) implements Stmt {
    public static ExpStmt of(Exp exp) {
        return new ExpStmt(exp);
    }
}
