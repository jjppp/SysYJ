package org.jjppp.runtime;

import org.jjppp.ast.exp.Exp;

import java.util.List;

/**
 * values = {exp ... exp}
 *
 * @param exps
 */
public record ArrVal(List<Exp> exps) implements Val {
    public static ArrVal of(List<Exp> exps) {
        return new ArrVal(exps);
    }

    @Override
    public int compareTo(Val val) {
        throw new UnsupportedOperationException("compare arr");
    }

    @Override
    public int toInt() {
        throw new UnsupportedOperationException("arr toInt");
    }
}
