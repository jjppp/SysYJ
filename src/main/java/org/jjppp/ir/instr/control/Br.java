package org.jjppp.ir.instr.control;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Set;

public final class Br extends Instr {
    private final Var cond;
    private final Label sTru;
    private final Label sFls;

    public Br(Var cond, Label sTru, Label sFls) {
        this.cond = cond;
        this.sTru = sTru;
        this.sFls = sFls;
    }

    public static Br of(Var cond, Label sTru, Label sFls) {
        return new Br(cond, sTru, sFls);
    }

    public Var cond() {
        return cond;
    }

    public Label sTru() {
        return sTru;
    }

    public Label sFls() {
        return sFls;
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
}
