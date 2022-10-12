package org.jjppp.ir.instr;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Collections;
import java.util.Set;

public record Def(Var var, Ope rhs) implements Instr {
    public static Def of(Var var, Ope ope) {
        return new Def(var, ope);
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
        if (rhs instanceof Var) {
            return Set.of((Var) rhs);
        }
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return var.type() + " " + var + " = " + rhs;
    }
}
