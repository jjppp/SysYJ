package org.jjppp.ir.instr;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.Var;
import org.jjppp.ir.type.BaseType;
import org.jjppp.type.ArrType;

public final class GAlloc extends Alloc {
    public GAlloc(Var var, BaseType baseType, int length) {
        super(var, baseType, length);
    }

    public static GAlloc from(Var var, VarDecl varDecl) {
        if (!varDecl.isGlobal()) {
            throw new AssertionError("global");
        }
        return new GAlloc(var, BaseType.from(varDecl.type()), 1);
    }

    public static GAlloc from(Var var, ArrDecl arrDecl) {
        if (!arrDecl.isGlobal()) {
            throw new AssertionError("global");
        }
        ArrType arrType = arrDecl.type();
        return new GAlloc(var, BaseType.from(arrType.type()), arrType.length());
    }

    @Override
    public boolean hasEffect() {
        return true;
    }

    @Override
    public <R> R accept(InstrVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
