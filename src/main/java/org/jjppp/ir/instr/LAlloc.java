package org.jjppp.ir.instr;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.Var;
import org.jjppp.ir.type.BaseType;
import org.jjppp.type.ArrType;

public final class LAlloc extends Alloc {
    public LAlloc(Var var, BaseType baseType, int length) {
        super(var, baseType, length);
    }

    public static LAlloc from(Var var, VarDecl varDecl) {
        if (varDecl.isGlobal()) {
            throw new AssertionError("local");
        }
        return new LAlloc(var, BaseType.from(varDecl.type()), 1);
    }

    public static LAlloc from(Var var, ArrDecl arrDecl) {
        if (arrDecl.isGlobal()) {
            throw new AssertionError("local");
        }
        ArrType arrType = arrDecl.type();
        return new LAlloc(var, BaseType.from(arrType.type()), arrType.length());
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
