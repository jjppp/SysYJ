package org.jjppp.ir;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Loc;
import org.jjppp.ir.type.Type;

import java.util.Objects;

public record Var(Decl decl, Type type, int id) implements Ope {
    private static int varCount = -1;

    public static Var allocTmp(Type type) {
        varCount += 1;
        VarDecl varDecl = VarDecl.of(Objects.toString(varCount), null, null, false);
        return new Var(varDecl, type, varCount);
    }

    public static Var from(VarDecl varDecl) {
        Var newVar;
        if (!varDecl.isGlobal()) {
            newVar = new Var(varDecl, Type.from(varDecl.type()), -1);
        } else {
            newVar = new Var(varDecl, Loc.of(BaseType.from(varDecl.type())), -1);
        }
        return newVar;
    }

    public static Var from(ArrDecl arrDecl) {
        return new Var(arrDecl, Loc.from(arrDecl), -1);
    }

    @Override
    public String toString() {
        if (decl.isGlobal()) {
            return "@" + decl().name();
        } else {
            return "%" + decl().name();
        }
    }
}
