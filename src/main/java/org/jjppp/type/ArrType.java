package org.jjppp.type;

import org.jjppp.runtime.BaseVal;

import java.util.List;

public record ArrType(BaseType type, int dim, List<Integer> widths, boolean isConst) implements Type {
    public static ArrType of(BaseType type, List<Integer> widths) {
        return new ArrType(type, widths.size(), widths, type.isConst());
    }

    public int length() {
        return widths.stream().reduce(1, (x, y) -> x * y);
    }

    @Override
    public int size() {
        return length() * type().size();
    }

    @Override
    public boolean isConst() {
        return isConst;
    }

    @Override
    public BaseVal defVal() {
        return type.defVal();
    }

    @Override
    public String toString() {
        return type + " " + widths;
    }
}
