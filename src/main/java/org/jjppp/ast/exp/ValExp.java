package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.runtime.Float;
import org.jjppp.runtime.Int;
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

    public static ValExp of(int value) {
        return ValExp.of(Int.from(value));
    }

    public static ValExp of(float value) {
        return ValExp.of(Float.from(value));
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
