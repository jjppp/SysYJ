package org.jjppp.ir.instr.control;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Collections;
import java.util.Set;

public record Ret(Ope retVal) implements Instr {
    @Override
    public boolean hasEffect() {
        return true;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Var var() {
        return null;
    }

    @Override
    public Set<Var> useSet() {
        if (retVal instanceof Var var) {
            return Set.of(var);
        }
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "ret " + retVal;
    }
}