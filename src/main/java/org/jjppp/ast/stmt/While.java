package org.jjppp.ast.stmt;

import org.jjppp.ast.cond.Cond;

public record While(Cond cond, Stmt body) implements Stmt {
}
