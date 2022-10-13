package org.jjppp.ir.instr;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record LibCall(Var var, LibFun libFun, List<Ope> args) implements Instr {
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
        return args().stream()
                .filter(Var.class::isInstance)
                .map(Var.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return var + " = call @" + libFun().name() + " " + args;
    }
}
