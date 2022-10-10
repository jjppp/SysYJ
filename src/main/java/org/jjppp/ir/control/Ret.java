package org.jjppp.ir.control;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Var;

public record Ret(Var var) implements Instr {
    @Override
    public String toString() {
        return "ret " + var;
    }
}
