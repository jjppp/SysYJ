package org.jjppp.ast.decl;

import org.jjppp.ast.exp.Exp;
import org.jjppp.type.ArrType;

public record ArrDecl(String name, ArrType type, Exp defValExp) implements Decl {
    public static ArrDecl of(String name, ArrType type, Exp defValExp) {
        return new ArrDecl(name, type, defValExp);
    }
}
