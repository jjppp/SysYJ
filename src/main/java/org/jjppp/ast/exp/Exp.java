package org.jjppp.ast.exp;

import org.jjppp.ast.ASTNode;
import org.jjppp.runtime.Val;
import org.jjppp.type.Type;

public interface Exp extends ASTNode {
    boolean isConst();

    Val constEval();

    Type type();
}
