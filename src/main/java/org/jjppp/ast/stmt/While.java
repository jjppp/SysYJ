package org.jjppp.ast.stmt;

import org.jjppp.ast.cond.Cond;

public record While(Cond cond, Stmt body) implements Stmt {
    public static While of(Cond cond, Stmt body) {
        return new While(cond, body);
    }
}
