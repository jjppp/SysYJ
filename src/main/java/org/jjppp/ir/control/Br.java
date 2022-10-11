package org.jjppp.ir.control;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Var;

import java.util.Set;

public final class Br implements Instr {
    private final Var cond;
    private final Label sTru, sFls;

    private boolean dead;

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
    public void setDead() {
        dead = true;
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public String toString() {
        return "br " + cond + " " + sTru + " " + sFls;
    }

    @Override
    public Set<Var> useSet() {
        return Set.of(cond);
    }

}
