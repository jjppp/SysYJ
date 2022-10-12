package org.jjppp.ir.control;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;

import java.util.Collections;
import java.util.Set;

public final class Label implements Instr {
    private static int labelCount = -1;
    private final String name;
    private final int id;

    public Label(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Label alloc(String name) {
        labelCount += 1;
        return new Label(name, labelCount);
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
    public Var var() {
        return null;
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
