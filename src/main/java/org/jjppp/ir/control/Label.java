package org.jjppp.ir.control;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Var;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class Label implements Instr {
    private static int labelCount = -1;
    private final String name;
    private final int id;
    private boolean dead = false;

    public Label(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Label alloc(String name) {
        labelCount += 1;
        return new Label(name, labelCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Label label) {
            return name.equals(label.name) && id == label.id && dead == label.dead;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dead);
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
        return "." + name + id + ":";
    }
}
