package org.jjppp.ir;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.OpExp;
import org.jjppp.ir.mem.GAlloc;
import org.jjppp.ir.mem.LAlloc;

public record Def(Var var, Exp rhs) implements Instr {
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
    public String toString() {
        return var.type() + " " + var + " = " + rhs;
    }
}
