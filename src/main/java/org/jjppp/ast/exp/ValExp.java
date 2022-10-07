package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.runtime.ArrVal;
import org.jjppp.runtime.Float;
import org.jjppp.runtime.Int;
import org.jjppp.runtime.Val;
import org.jjppp.type.FloatType;
import org.jjppp.type.IntType;
import org.jjppp.type.Type;
import org.jjppp.type.VoidType;

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

    @Override
    public Type type() {
        if (val instanceof Int) {
            return IntType.ofConst();
        } else if (val instanceof Float) {
            return FloatType.ofConst();
        } else if (val instanceof ArrVal) {
            throw new AssertionError("TODO");
        } else if (val instanceof Val.Void) {
            return VoidType.getInstance();
        }
        throw new AssertionError("should not reach here");
    }
}
