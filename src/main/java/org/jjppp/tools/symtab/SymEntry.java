package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.Decl;

public final class SymEntry implements Entry {
    private final Decl decl;
    private final boolean isGlobal;

    private SymEntry(Decl decl) {
        this.decl = decl;
        this.isGlobal = SymTab.getInstance().isGlobal();
    }

    public SymEntry(Decl decl, boolean isGlobal) {
        this.decl = decl;
        this.isGlobal = isGlobal;
    }

    static SymEntry from(Decl decl) {
        return new SymEntry(decl);
    }

    static SymEntry from(Decl decl, boolean isGlobal) {
        return new SymEntry(decl, isGlobal);
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
