package org.jjppp.ir.instr;

import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BiExp extends Instr {
    private final Var var;
    private final BiOp op;
    private final Ope lhs;
    private final Ope rhs;

    public BiExp(Var var, BiOp op, Ope lhs, Ope rhs) {
        this.var = var;
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Var var() {
        return var;
    }

    public BiOp op() {
        return op;
    }

    public Ope lhs() {
        return lhs;
    }

    public Ope rhs() {
        return rhs;
    }

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
        return super.toString() + var + " = " + op + " " + lhs + " " + rhs;
    }
}
