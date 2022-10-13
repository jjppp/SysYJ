package org.jjppp.ast.exp;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.runtime.ArrVal;
import org.jjppp.runtime.Val;
import org.jjppp.type.ArrType;
import org.jjppp.type.Type;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ArrValExp(List<Exp> exps) implements Exp {
    public static ArrValExp of(List<Exp> exps) {
        return new ArrValExp(exps);
    }

    public Queue<Exp> toLinear(ArrType arrType) {
        List<Exp> expList =
                exps.stream().flatMap(x -> {
                    if (x instanceof ArrValExp arrValExp) {
                        if (arrType.subType() instanceof ArrType subArrType) {
                            return arrValExp.toLinear(subArrType).stream();
                        } else {
                            throw new RuntimeException("");
                        }
                    } else return Stream.of(x);
                }).toList();
        return Stream.concat(
                expList.stream(),
                Collections.nCopies(
                        arrType.totalLength() - expList.size(),
                        (ValExp) null).stream()
        ).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Val constEval() {
        return ArrVal.of(exps.stream().map(Exp::constEval).collect(Collectors.toList()));
    }

    @Override
    public Type type() {
        throw new AssertionError("TODO");
    }
}
