package org.jjppp.ir;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.type.LocType;

public record Alloc(Var var) {
    public static Alloc from(VarDecl varDecl) {
        return new Alloc(Var.from(varDecl));
    }

    public static Alloc from(ArrDecl arrDecl) {
        return new Alloc(Var.from(arrDecl));
    }

    @Override
    public String toString() {
        if (var.type() instanceof LocType) {
            return "alloc " + var()
                    + "[" + size() + "] "
                    + ((LocType) var().type()).baseType();
        } else {
            return "alloc " + var() + " " + var().type();
        }
    }

    public int size() {
        return var().decl().size();
    }
}
