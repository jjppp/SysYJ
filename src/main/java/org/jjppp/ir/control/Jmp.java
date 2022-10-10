package org.jjppp.ir.control;

import org.jjppp.ir.Instr;

public record Jmp(Label target) implements Instr {
    public static Jmp of(Label target) {
        return new Jmp(target);
    }

    @Override
    public String toString() {
        return "jmp " + target;
    }
}
