package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.Decl;
import org.jjppp.type.Type;

public final class SymEntry {
    private final Decl decl;
    private int addr;

    private SymEntry(int addr, Decl decl) {
        this.addr = addr;
        this.decl = decl;
    }

    private SymEntry(Decl decl) {
        this.decl = decl;
        this.addr = -1;
    }

    public static SymEntry from(Decl decl) {
        return new SymEntry(decl);
    }

    public String getName() {
        return decl.name();
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
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
}
