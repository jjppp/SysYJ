package org.jjppp.ir;

import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.control.Label;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Type;

import java.util.List;

public record Fun(Signature signature, List<Instr> body) {
    public static Fun of(String name, BaseType retType, List<Type> argTypes, List<Var> args, List<Instr> body) {
        return new Fun(new Signature(name, retType, argTypes, args), body);
    }

    @Override
    public String toString() {
        return "fun @" + signature.name
                + "(" + signature.argTypes() + ")"
                + ": " + signature.retType() + " {"
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

    public record Signature(String name, BaseType retType, List<Type> argTypes, List<Var> args) {
        @Override
        public String toString() {
            return retType + " " + name + "(" + argTypes + ")";
        }
    }
}
