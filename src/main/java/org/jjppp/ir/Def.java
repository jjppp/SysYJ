package org.jjppp.ir;

import org.jjppp.ast.exp.OpExp;

public record Def(Var var, Exp rhs) implements IR {
    public static Def of(Var var) {
        return Def.of(var, var.type().defVal());
    }

    public static Def of(Var var, Ope ope) {
        return Def.of(var, new Exp.UnExp(OpExp.UnOp.NONE, ope));
    }

    public static Def of(Var var, Exp rhs) {
        return new Def(var, rhs);
    }

    @Override
    public String toString() {
        return var.type() + " " + var + " = " + rhs;
    }
}
