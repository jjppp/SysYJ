package org.jjppp.ast.exp;

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
    public Val eval() {
        return val;
    }
}
