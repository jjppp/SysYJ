package org.jjppp.ast.stmt;

import org.jjppp.ast.cond.Cond;

public record If(Cond cond, Stmt sTru, Stmt sFls) implements Stmt {
}
