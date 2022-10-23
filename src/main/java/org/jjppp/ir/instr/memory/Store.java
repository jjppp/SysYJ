package org.jjppp.ir.instr.memory;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Store extends Instr {
    private final Var var;
    private final Ope rhs;

    public Store(Var var, Ope rhs) {
        this.var = var;
        this.rhs = rhs;
    }

    public Var var() {
        return var;
    }

    public Ope rhs() {
        return rhs;
    }

    @Override
    public boolean hasEffect() {
        return true;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
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
