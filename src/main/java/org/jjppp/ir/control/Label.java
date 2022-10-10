package org.jjppp.ir.control;

import org.jjppp.ir.Instr;

public record Label(String name, int id) implements Instr {
    private static int labelCount = -1;

    public static Label alloc(String name) {
        labelCount += 1;
        return new Label(name, labelCount);
    }

    @Override
    public String toString() {
        return "." + name + id + ":";
    }
}
