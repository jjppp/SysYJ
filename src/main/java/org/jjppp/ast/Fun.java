package org.jjppp.ast;

import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.stmt.Stmt;

public record Fun(FunDecl def, Stmt body) {
}
