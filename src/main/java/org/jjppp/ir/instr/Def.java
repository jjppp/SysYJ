package org.jjppp.ir.instr;

import org.jjppp.ir.Var;

public abstract class Def extends Instr {
    private Var var;

    public Def(Var var) {
        this.var = var;
    }

    public Var var() {
        return var;
    }

    public void setVar(Var var) {
        this.var = var;
    }
}
