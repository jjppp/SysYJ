package org.jjppp.ir.instr;

import org.jjppp.ir.Fun;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Call extends Instr {
    private final Var var;
    private final Fun.Signature fun;
    private final List<Ope> args;

    public Call(Var var, Fun.Signature fun, List<Ope> args) {
        this.var = var;
        this.fun = fun;
        this.args = args;
    }

    public Var var() {
        return var;
    }

    public Fun.Signature fun() {
        return fun;
    }

    public List<Ope> args() {
        return args;
    }

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
        return var + " = call @" + fun.name() + " " + args;
    }
}
