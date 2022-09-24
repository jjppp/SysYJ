package org.jjppp.runtime;

import java.util.List;

public record ArrVal(List<Val> vals) implements Val {
    @Override
    public int compareTo(Val val) {
        throw new RuntimeException("unsupported");
    }
}
