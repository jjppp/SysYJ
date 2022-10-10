package org.jjppp.ir.mem;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.type.BaseType;
import org.jjppp.type.ArrType;

public final class GAlloc extends Alloc {
    public GAlloc(BaseType baseType, int length) {
        super(baseType, length);
    }

    public static GAlloc from(VarDecl varDecl) {
        if (!varDecl.isGlobal()) {
            throw new AssertionError("global");
        }
        return new GAlloc(BaseType.from(varDecl.type()), 1);
    }

    public static GAlloc from(ArrDecl arrDecl) {
        if (!arrDecl.isGlobal()) {
            throw new AssertionError("global");
        }
        ArrType arrType = arrDecl.type();
        return new GAlloc(BaseType.from(arrType.type()), arrType.length());
    }
}
