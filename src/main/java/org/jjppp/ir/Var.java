package org.jjppp.ir;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.type.LocType;
import org.jjppp.type.Type;

import java.util.Objects;

public record Var(Decl decl, Type type, int id) implements Ope {
    private static int varCount = -1;

    public static Var allocTmp(Type type) {
        varCount += 1;
        VarDecl varDecl = VarDecl.of(Objects.toString(varCount), type, null, false);
        return new Var(varDecl, type, varCount);
    }

    public static Var from(VarDecl varDecl) {
        return new Var(varDecl, varDecl.type(), -1);
    }

    public static Var from(ArrDecl arrDecl) {
        return new Var(arrDecl, LocType.from(arrDecl), -1);
    }

    @Override
    public String toString() {
        if (decl.isGlobal()) {
            return "@" + decl.name();
        } else {
            return "%" + decl().name();
        }
    }
}
