package org.jjppp.ast.exp;

import org.jjppp.ast.ASTNode;
import org.jjppp.runtime.Val;

public interface Exp extends ASTNode {
    Val constEval();
}
