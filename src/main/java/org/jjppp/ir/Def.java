package org.jjppp.ir;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.OpExp;
import org.jjppp.ir.mem.GAlloc;
import org.jjppp.ir.mem.LAlloc;

import java.util.Objects;
import java.util.Set;

public final class Def implements Instr {
    private final Var var;
    private Exp rhs;
    private boolean dead = false;

    public Def(Var var, Exp rhs) {
        this.var = var;
        this.rhs = rhs;
    }

    public static Def of(Var var, Ope ope) {
        return Def.of(var, new Exp.UnExp(OpExp.UnOp.NONE, ope));
    }

    public static Def of(Var var, Exp rhs) {
        return new Def(var, rhs);
    }

    public static Def from(Var var, ArrDecl arrDecl) {
        if (arrDecl.isGlobal()) {
            return new Def(var, GAlloc.from(arrDecl));
        } else {
            return new Def(var, LAlloc.from(arrDecl));
        }
    }

    public static Def from(Var var, VarDecl varDecl) {
        if (varDecl.isGlobal()) {
            return new Def(var, GAlloc.from(varDecl));
        } else {
            return new Def(var, LAlloc.from(varDecl));
        }
    }

    @Override
    public void setDead() {
        dead = true;
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public Set<Var> useSet() {
        return rhs().useSet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Def def) {
            return var.equals(def.var)
                    && rhs.equals(def.rhs);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(var, rhs);
    }

    public Var var() {
        return var;
    }

    public Exp rhs() {
        return rhs;
    }

    public void setRhs(Exp rhs) {
        this.rhs = rhs;
    }

    public void setRhs(Ope ope) {
        Objects.requireNonNull(ope);
        setRhs(new Exp.UnExp(OpExp.UnOp.NONE, ope));
    }

    @Override
    public String toString() {
        return var.type() + " " + var + " = " + rhs;
    }
}
