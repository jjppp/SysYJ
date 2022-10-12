package org.jjppp.ir;

import org.jjppp.ir.control.Label;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Type;

import java.util.List;

public record Fun(String name, BaseType retType, List<Type> argTypes, List<Instr> body) {
    public static Fun of(String name, BaseType retType, List<Type> argTypes, List<Instr> body) {
        return new Fun(name, retType, argTypes, body);
    }

    @Override
    public String toString() {
        return "fun @" + name
                + "(" + argTypes() + ")"
                + ": " + retType() + " {"
                + body.stream()
                .map(x -> {
                    if (x instanceof Label) {
                        return x.toString();
                    } else {
                        return "  " + x;
                    }
                })
                .reduce("", (x, y) -> x + "\n" + y)
                + "\n}";
    }
}
