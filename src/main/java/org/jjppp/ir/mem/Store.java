package org.jjppp.ir.mem;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

/**
 * loc[off] = rhs
 *
 * @param var: global var & arr
 * @param rhs
 */
public record Store(Var var, Ope rhs) implements Instr {
    @Override
    public String toString() {
        return "*" + var + " = " + rhs;
    }
}
