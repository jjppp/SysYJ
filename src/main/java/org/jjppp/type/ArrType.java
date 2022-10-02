package org.jjppp.type;

import java.util.List;

public record ArrType(BaseType type, int dim, List<Integer> widths, boolean isConst) implements Type {
    public static ArrType of(BaseType type, List<Integer> widths) {
        return new ArrType(type, widths.size(), widths, type.isConst());
    }

    @Override
    public int size() {
        return type.size() * widths.stream().reduce(1, (x, y) -> x * y);
    }

    @Override
    public boolean isConst() {
        return isConst;
    }
}
