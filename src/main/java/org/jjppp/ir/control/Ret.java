package org.jjppp.ir.control;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Var;

import java.util.Set;

public final class Ret implements Instr {
    private final Var var;
    private boolean dead = false;

    public Ret(Var var) {
        this.var = var;
    }

    @Override
    public void setDead() {
        dead = true;
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public Set<Var> useSet() {
        return Set.of(var);
    }

    @Override
    public String toString() {
        return "ret " + var;
    }
}
