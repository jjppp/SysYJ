package org.jjppp.ast.cond;

import org.jjppp.ast.Binary;

/**
 * Binary Conditions
 * lhs && rhs
 * lhs || rhs
 */
public final class BinCond extends Binary<Cond> implements Cond {
    private final Op op;

    public BinCond(Op op, Cond lhs, Cond rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    @Override
    public boolean isTrue() {
        return false;
    }

    private enum Op {
        AND, OR
    }
}
