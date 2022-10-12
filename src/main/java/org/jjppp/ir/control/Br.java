package org.jjppp.ir.control;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Set;

public record Br(Var cond, Label sTru, Label sFls) implements Instr {
    public static Br of(Var cond, Label sTru, Label sFls) {
        return new Br(cond, sTru, sFls);
    }

    @Override
    public boolean hasEffect() {
        return true;
    }

    @Override
    public String toString() {
        return "br " + cond + " " + sTru + " " + sFls;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Set<Var> useSet() {
        return Set.of(cond);
    }

    @Override
    public Var var() {
        return null;
    }
}
