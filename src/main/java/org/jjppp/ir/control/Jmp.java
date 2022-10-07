package org.jjppp.ir.control;

import org.jjppp.ir.IR;

public record Jmp(Label target) implements IR {
    public static Jmp of(Label target) {
        return new Jmp(target);
    }

    @Override
    public String toString() {
        return "jmp " + target;
    }
}
