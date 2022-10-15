package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.exp.ArrValExp;
import org.jjppp.type.ArrType;

import java.util.ArrayList;

public record ArrDecl(String name, ArrType type, ArrValExp arrValExp, boolean isGlobal, int id) implements Decl {
    private static int ARRDECL_COUNT = 0;

    public static ArrDecl of(String name, ArrType type, ArrValExp arrValExp, boolean isGlobal) {
//        throw new UnsupportedOperationException("array not supported");
        ARRDECL_COUNT += 1;
        if (arrValExp == null) {
            arrValExp = ArrValExp.of(new ArrayList<>());
        }
        return new ArrDecl(name, type, arrValExp, isGlobal, ARRDECL_COUNT);
    }

    @Override
    public String toString() {
        return "arr [" + type().length() + " x " + type().subType() + "]";
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ArrType type() {
        return type;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isConst() {
        return type.isConst();
    }
}
