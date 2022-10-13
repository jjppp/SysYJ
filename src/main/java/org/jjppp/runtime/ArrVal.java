package org.jjppp.runtime;

import java.util.List;

/**
 * values = {exp ... exp}
 *
 * @param exps
 */
public record ArrVal(List<Val> exps) implements Val {
    public static ArrVal of(List<Val> exps) {
        return new ArrVal(exps);
    }

    @Override
    public int compareTo(Val val) {
        throw new UnsupportedOperationException("compare arr");
    }

    @Override
    public Int toInt() {
        throw new UnsupportedOperationException("arr toInt");
    }

    @Override
    public Float toFloat() {
        throw new UnsupportedOperationException();
    }
}
