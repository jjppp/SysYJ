package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;

public final class SymEntry implements Entry {
    private final Decl decl;
    private final boolean isGlobal;
    private int addr;
    private int size;

    private SymEntry(int addr, Decl decl) {
        this.addr = addr;
        this.decl = decl;
        this.isGlobal = SymTab.isGlobal();
    }

    static SymEntry from(FunDecl funDecl) {
        return new SymEntry(-1, funDecl);
    }

    static SymEntry from(Decl decl) {
        return new SymEntry(-1, decl);
    }

    public int getSize() {
        return size;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }

    @Override
    public Decl getDecl() {
        return decl;
    }

    @Override
    public boolean isGlobal() {
        return isGlobal;
    }
}
