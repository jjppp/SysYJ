package org.jjppp.ir.mem;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.type.BaseType;
import org.jjppp.type.ArrType;

public final class LAlloc extends Alloc {
    public LAlloc(BaseType baseType, int length) {
        super(baseType, length);
    }

    public static LAlloc from(VarDecl varDecl) {
        if (varDecl.isGlobal()) {
            throw new AssertionError("local");
        }
        return new LAlloc(BaseType.from(varDecl.type()), 1);
    }

    public static LAlloc from(ArrDecl arrDecl) {
        if (arrDecl.isGlobal()) {
            throw new AssertionError("local");
        }
        ArrType arrType = arrDecl.type();
        return new LAlloc(BaseType.from(arrType.type()), arrType.length());
    }
}
