package org.jjppp.type;

import java.util.List;

public record ArrType(BaseType type, int dim, List<Integer> widths, boolean isConst) implements Type {
    public static ArrType of(BaseType type, int dim, List<Integer> widths, boolean isConst) {
        return new ArrType(type, dim, widths, isConst);
    }

    @Override
    public boolean isConst() {
        return isConst;
    }
}
