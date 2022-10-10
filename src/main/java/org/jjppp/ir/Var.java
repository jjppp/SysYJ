package org.jjppp.ir;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Loc;
import org.jjppp.ir.type.Type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Var(Decl decl, Type type, int id) implements Ope {
    private static final Set<Var> varSet = new HashSet<>();
    private static int varCount = -1;

    public static Set<Var> varSet() {
        return varSet;
    }

    public static Var allocTmp(Type type) {
        varCount += 1;
        VarDecl varDecl = VarDecl.of(Objects.toString(varCount), null, null, false);
        Var newVar = new Var(varDecl, type, varCount);
        varSet.add(newVar);
        return newVar;
    }

    public static Var from(VarDecl varDecl) {
        Var newVar;
        if (!varDecl.isGlobal()) {
            newVar = new Var(varDecl, Type.from(varDecl.type()), -1);
        } else {
            newVar = new Var(varDecl, Loc.of(BaseType.from(varDecl.type())), -1);
        }
        varSet.add(newVar);
        return newVar;
    }

    public static Var from(ArrDecl arrDecl) {
        Var newVar = new Var(arrDecl, Loc.from(arrDecl), -1);
        varSet.add(newVar);
        return newVar;
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
