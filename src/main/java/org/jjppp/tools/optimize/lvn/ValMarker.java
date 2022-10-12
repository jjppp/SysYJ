package org.jjppp.tools.optimize.lvn;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.*;
import org.jjppp.runtime.BaseVal;

import static org.jjppp.tools.optimize.lvn.ValTab.Val;

public final class ValMarker implements InstrVisitor<Val> {
    private final ValTab valTab;

    public ValMarker(ValTab valTab) {
        this.valTab = valTab;
    }

    public int from(Ope ope) {
        Val rawVal = new Val.RawVal(ope);
        if (ope instanceof BaseVal) {
            if (!valTab.contains(rawVal)) return valTab.alloc(rawVal, ope);
            return valTab.get(rawVal);
        } else if (ope instanceof Var var) {
            Integer holdingID = valTab.holding(var);
            if (holdingID != null) {
                return holdingID;
            } else {
                int id = valTab.alloc(rawVal, ope);
                valTab.hold(ope, id);
                return id;
            }
        }
        throw new AssertionError("TODO");
    }

    public Val mark(Instr instr) {
        return instr.accept(this);
    }

    @Override
    public Val visit(BiExp exp) {
        return new Val.BiVal(exp.op(), from(exp.lhs()), from(exp.rhs()));
    }

    @Override
    public Val visit(UnExp exp) {
        return new Val.UnVal(exp.op(), from(exp.sub()));
    }

    @Override
    public Val visit(Load load) {
        return Val.LdVal.alloc(from(load.loc()));
    }
}
