package org.jjppp.ast.stmt;

import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.LVal;

public record Assign(LVal lhs, Exp rhs) implements Stmt {
}
