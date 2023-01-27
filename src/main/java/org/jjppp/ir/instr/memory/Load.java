package org.jjppp.ir.instr.memory;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Def;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Load extends Def {
    private final Ope loc;

    public Load(Var var, Ope loc) {
        super(var);
        this.loc = loc;
    }

    public Ope loc() {
        return loc;
    }

    @Override
    public boolean hasEffect() {
        return false;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Set<Var> useSet() {
        return Stream.of(var(), loc)
                .filter(Var.class::isInstance)
                .map(Var.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return var() + " = " + "*" + loc;
    }
}
