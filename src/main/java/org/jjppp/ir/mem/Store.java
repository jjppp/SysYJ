package org.jjppp.ir.mem;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Store implements Instr {
    private final Var var;
    private final Ope rhs;
    private boolean dead = false;

    public Store(Var var, Ope rhs) {
        this.var = var;
        this.rhs = rhs;
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
