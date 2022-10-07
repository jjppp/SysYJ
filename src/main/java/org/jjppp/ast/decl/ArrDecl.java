package org.jjppp.ast.decl;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.type.ArrType;

import java.util.List;

public record ArrDecl(String name, ArrType type, boolean isGlobal) implements Decl {
    public static ArrDecl of(String name, ArrType type, boolean isGlobal) {
        throw new UnsupportedOperationException("array not supported");
//        return new ArrDecl(name, type, isGlobal);
    }

    private String printWidths(List<Integer> widths) {
        return widths.stream()
                .map(Object::toString)
                .reduce((x, y) -> x + "][" + y)
                .orElse("");
    }

    @Override
    public String toString() {
        return type().type()
                + " " + name()
                + "[" + printWidths(type().widths()) + "]";
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
