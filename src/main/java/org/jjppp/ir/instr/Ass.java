package org.jjppp.ir.instr;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Collections;
import java.util.Set;

public final class Ass extends Def {
    private final Ope rhs;

    public Ass(Var var, Ope rhs) {
        super(var);
        this.rhs = rhs;
    }

    public static Ass of(Var var, Ope ope) {
        return new Ass(var, ope);
    }

    public Ope rhs() {
        return rhs;
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
        return super.toString() + var().type() + " " + var() + " = " + rhs;
    }
}
