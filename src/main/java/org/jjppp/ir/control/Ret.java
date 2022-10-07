package org.jjppp.ir.control;

import org.jjppp.ir.IR;
import org.jjppp.ir.Var;

import java.util.Optional;

public record Ret(Optional<Var> var) implements IR {
    @Override
    public String toString() {
        return "ret " + var;
    }
}
