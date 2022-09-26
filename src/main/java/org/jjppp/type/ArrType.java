package org.jjppp.type;

import java.util.List;

public record ArrType(BaseType type, int dim, List<Integer> widths) implements Type {
    public static ArrType of(BaseType type, int dim, List<Integer> widths) {
        return new ArrType(type, dim, widths);
    }
}
