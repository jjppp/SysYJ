package org.jjppp.ir.instr;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.memory.Alloc;
import org.jjppp.ir.instr.memory.GAlloc;
import org.jjppp.ir.instr.memory.LAlloc;

import java.util.Set;

public interface Instr {
    static Alloc from(Var var, ArrDecl arrDecl) {
        if (arrDecl.isGlobal()) {
            return GAlloc.from(var, arrDecl);
        } else {
            return LAlloc.from(var, arrDecl);
        }
    }

    static Alloc from(Var var, VarDecl varDecl) {
        if (varDecl.isGlobal()) {
            return GAlloc.from(var, varDecl);
        } else {
            return LAlloc.from(var, varDecl);
        }
    }

    <R> R accept(InstrVisitor<R> visitor);

    Set<Var> useSet();

    Var var();

    boolean hasEffect();
}
