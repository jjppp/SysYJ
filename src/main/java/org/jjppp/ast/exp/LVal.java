package org.jjppp.ast.exp;

import org.jjppp.ast.decl.Decl;

public interface LVal extends Exp {
    Decl getDecl();
}
