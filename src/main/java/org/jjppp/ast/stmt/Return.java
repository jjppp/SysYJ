package org.jjppp.ast.stmt;

import org.jjppp.ast.exp.Exp;

import java.util.Optional;

public record Return(Optional<Exp> exp) implements Stmt {
    public static Return of(Exp exp) {
        return new Return(Optional.ofNullable(exp));
    }
}
