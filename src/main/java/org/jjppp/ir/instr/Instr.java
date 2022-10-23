package org.jjppp.ir.instr;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.instr.memory.Alloc;
import org.jjppp.ir.instr.memory.GAlloc;
import org.jjppp.ir.instr.memory.LAlloc;

import java.util.Optional;
import java.util.Set;

public abstract class Instr {
    private Block belongTo = null;
    private boolean isInv = false;

    public static Alloc from(Var var, ArrDecl arrDecl) {
        if (arrDecl.isGlobal()) {
            return GAlloc.from(var, arrDecl);
        } else {
            return LAlloc.from(var, arrDecl);
        }
    }

    public static Alloc from(Var var, VarDecl varDecl) {
        if (varDecl.isGlobal()) {
            return GAlloc.from(var, varDecl);
        } else {
            return LAlloc.from(var, varDecl);
        }
    }

    @Override
    public String toString() {
        if (isInv()) {
            return "=> ";
        } else {
            return "";
        }
    }

    public abstract <R> R accept(InstrVisitor<R> visitor);

    public void setBelongTo(Block block) {
        this.belongTo = block;
    }

    public Block belongTo() {
        return belongTo;
    }

    public abstract Set<Var> useSet();

    public Optional<Var> defSet() {
        return Optional.ofNullable(var());
    }

    public abstract Var var();

    public int index() {
        return belongTo().instrList().indexOf(this);
    }

    public boolean isInv() {
        return isInv;
    }

    public void setInv(boolean isInv) {
        this.isInv = isInv;
    }

    public abstract boolean hasEffect();
}
