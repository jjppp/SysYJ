package org.jjppp.ir.mem;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Store implements Instr {
    private Var var;
    private Ope rhs;
    private boolean dead = false;

    public Store(Var var, Ope rhs) {
        this.var = var;
        this.rhs = rhs;
    }

    public Var var() {
        return var;
    }

    public void setRhs(Ope rhs) {
        this.rhs = rhs;
    }

    public void setVar(Var var) {
        this.var = var;
    }

    public Ope rhs() {
        return rhs;
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public void setDead() {
        dead = true;
    }

    @Override
    public Set<Var> useSet() {
        return Stream.of(rhs, var)
                .filter(Var.class::isInstance)
                .map(Var.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "*" + var + " = " + rhs;
    }
}
