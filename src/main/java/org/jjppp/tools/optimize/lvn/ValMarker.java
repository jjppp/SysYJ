package org.jjppp.tools.optimize.lvn;

import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.BiExp;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;
import org.jjppp.ir.instr.UnExp;
import org.jjppp.ir.instr.memory.Load;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Int;

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
        int lhs = from(exp.lhs());
        int rhs = from(exp.rhs());
        BiOp op = exp.op();
        Ope lhsOpe = valTab.belong(lhs);
        Ope rhsOpe = valTab.belong(rhs);
        if (lhsOpe instanceof Int lhsVal && rhsOpe instanceof Int rhsVal) {
            return new Val.RawVal(op.apply(lhsVal, rhsVal));
        }
        switch (op) {
            case ADD -> {
                if (lhsOpe.equals(Int.from(0))) {
                    return new Val.RawVal(rhsOpe);
                } else if (rhsOpe.equals(Int.from(0))) {
                    return new Val.RawVal(lhsOpe);
                } else {
                    return new Val.BiVal(op, lhs, rhs);
                }
            }
            case MUL -> {
                if (lhsOpe.equals(Int.from(1))) {
                    return new Val.RawVal(rhsOpe);
                } else if (rhsOpe.equals(Int.from(1))) {
                    return new Val.RawVal(lhsOpe);
                } else if (lhsOpe.equals(Int.from(0))) {
                    return new Val.RawVal(Int.from(0));
                } else if (rhsOpe.equals(Int.from(0))) {
                    return new Val.RawVal(Int.from(0));
                } else {
                    return new Val.BiVal(op, lhs, rhs);
                }
            }
            default -> {
                return new Val.BiVal(op, lhs, rhs);
            }
        }
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
