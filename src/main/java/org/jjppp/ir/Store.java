package org.jjppp.ir;

/**
 * loc[off] = rhs
 *
 * @param var: global var & arr
 * @param off: offset
 * @param rhs
 */
public record Store(Var var, Var off, Ope rhs) implements IR {
    @Override
    public String toString() {
        return var + "[" + off + "] = " + rhs;
    }
}
