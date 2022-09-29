package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.runtime.Val;

/**
 * Value Exp
 *
 * @param val
 */
public record ValExp(Val val) implements Exp {
    public static ValExp of(Val val) {
        return new ValExp(val);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        return val;
    }
}
