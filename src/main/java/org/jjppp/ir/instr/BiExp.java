package org.jjppp.ir.instr;

import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record BiExp(Var var, BiOp op, Ope lhs, Ope rhs) implements Instr {
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
        return Stream.of(lhs, rhs)
                .filter(Var.class::isInstance)
                .map(Var.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return var + " = " + op + " " + lhs + " " + rhs;
    }
}
