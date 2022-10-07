package org.jjppp.ir.control;

import org.jjppp.ir.IR;
import org.jjppp.ir.Var;

public record Br(Var cond, Label sTru, Label sFls) implements IR {
    public static Br of(Var cond, Label sTru, Label sFls) {
        return new Br(cond, sTru, sFls);
    }

    @Override
    public String toString() {
        return "br " + cond + " " + sTru + " " + sFls;
    }
}
