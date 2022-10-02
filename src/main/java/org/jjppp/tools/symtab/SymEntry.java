package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.runtime.Val;
import org.jjppp.type.Type;

import java.util.Optional;

public final class SymEntry {
    private final Decl decl;
    private final Val defVal;
    private final boolean isGlobal;
    private int addr;
    private int size;

    private SymEntry(int addr, Decl decl, Val defVal) {
        this.addr = addr;
        this.decl = decl;
        this.isGlobal = SymTab.isGlobal();
        this.defVal = defVal;
    }

    public static SymEntry from(FunDecl funDecl) {
        return new SymEntry(-1, funDecl, null);
    }

    public static SymEntry from(Decl decl, Val defVal) {
        if (defVal != null && !SymTab.isGlobal()) {
            throw new AssertionError("local symbol should not have defVal");
        }
        return new SymEntry(-1, decl, defVal);
    }

    public String getName() {
        return decl.name();
    }

    public Optional<Val> defVal() {
        return Optional.ofNullable(defVal);
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }

    public int size() {
        return size;
    }

    public Type getType() {
        return decl.type();
    }

    public Decl getDecl() {
        return decl;
    }

    public boolean isConst() {
        return decl.isConst();
    }

    public boolean isGlobal() {
        return isGlobal;
    }
}
