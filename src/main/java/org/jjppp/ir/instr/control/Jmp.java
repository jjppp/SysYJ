package org.jjppp.ir.instr.control;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Collections;
import java.util.Set;

public final class Jmp extends Instr {
    private final Label target;

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
    public boolean hasEffect() {
        return true;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
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
