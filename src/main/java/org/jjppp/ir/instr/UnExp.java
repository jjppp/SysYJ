package org.jjppp.ir.instr;

import org.jjppp.ast.exp.op.UnOp;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record UnExp(Var var, UnOp op, Ope sub) implements Instr {
    @Override
    public boolean hasEffect() {
        return false;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Set<Var> useSet() {
        return Stream.of(sub)
                .filter(Var.class::isInstance)
                .map(Var.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return var + " = " + op + " " + sub;
    }
}
