package org.jjppp.ir.instr;

import org.jjppp.ast.exp.op.UnOp;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UnExp extends Instr {
    private final Var var;
    private final UnOp op;
    private final Ope sub;

    public UnExp(Var var, UnOp op, Ope sub) {
        this.var = var;
        this.op = op;
        this.sub = sub;
    }

    public Var var() {
        return var;
    }

    public UnOp op() {
        return op;
    }

    public Ope sub() {
        return sub;
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
        return Stream.of(sub)
                .filter(Var.class::isInstance)
                .map(Var.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return super.toString() + var + " = " + op + " " + sub;
    }
}
