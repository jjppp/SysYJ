package org.jjppp.ir.control;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Var;

import java.util.Collections;
import java.util.Set;

public final class Jmp implements Instr {
    private final Label target;
    private boolean dead = false;

    public Jmp(Label target) {
        this.target = target;
    }

    public static Jmp of(Label target) {
        return new Jmp(target);
    }

    public Label target() {
        return target;
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
    public Set<Var> useSet() {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "jmp " + target;
    }
}
