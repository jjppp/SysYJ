package org.jjppp.ir.instr.control;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Collections;
import java.util.Set;

public final class Label extends Instr {
    private final String name;
    private final int id;

    public Label(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
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
        return "." + name + id + ":";
    }
}
