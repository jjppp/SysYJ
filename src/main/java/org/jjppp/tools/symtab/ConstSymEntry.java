package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.Decl;
import org.jjppp.runtime.Val;

import java.util.Optional;

public final class ConstSymEntry implements Entry {
    private final Decl decl;
    private final Val defVal;
    private final boolean isGlobal;

    private ConstSymEntry(Decl decl, Val defValExp) {
        this.decl = decl;
        this.isGlobal = SymTab.getInstance().isGlobal();
        this.defVal = defValExp;
    }

    static ConstSymEntry from(Decl decl, Val defValExp) {
        return new ConstSymEntry(decl, defValExp);
    }

    public Optional<Val> defVal() {
        return Optional.ofNullable(defVal);
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
