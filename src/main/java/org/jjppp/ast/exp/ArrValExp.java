package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.runtime.ArrVal;
import org.jjppp.runtime.Val;

import java.util.List;
import java.util.stream.Collectors;

public record ArrValExp(List<Exp> exps, boolean isLinear) implements Exp {
    public static ArrValExp of(List<Exp> exps, boolean isLinear) {
        return new ArrValExp(exps, isLinear);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        return ArrVal.of(exps.stream().map(Exp::constEval).collect(Collectors.toList()));
    }
}
