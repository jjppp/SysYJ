package org.jjppp.ir.control;

import org.jjppp.ir.IR;

public record Label(String name, int id) implements IR {
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
