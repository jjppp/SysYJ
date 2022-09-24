package org.jjppp.ast.exp;

import org.jjppp.ast.Binary;
import org.jjppp.runtime.Val;

/**
 * Binary Exp
 */
public final class BinExp extends Binary<Exp> implements Exp {
    @Override
    public Val eval() {
        throw new AssertionError("TODO");
    }

    private enum Op {
        ADD, SUB, MUL, DIV, MOD
    }
}
