package org.jjppp.tools.optimize.lvn;

import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.instr.control.Br;
import org.jjppp.ir.instr.control.Jmp;
import org.jjppp.ir.instr.control.Label;
import org.jjppp.ir.instr.control.Ret;
import org.jjppp.ir.instr.*;
import org.jjppp.ir.instr.memory.GAlloc;
import org.jjppp.ir.instr.memory.LAlloc;
import org.jjppp.ir.instr.memory.Load;
import org.jjppp.ir.instr.memory.Store;

import java.util.stream.Collectors;

import static org.jjppp.tools.optimize.lvn.ValTab.Val;

public final class LVN implements InstrVisitor<Instr> {
    private final ValTab valTab = new ValTab();
    private final ValMarker marker = new ValMarker(valTab);

    public static Block doLVN(Block input) {
        LVN lvn = new LVN();
        return new Block(input.instrList().stream()
                .map(lvn::doLVN)
                .collect(Collectors.toList()));
    }

    public Instr doLVN(Instr instr) {
        return instr.accept(this);
    }

    @Override
    public Instr visit(GAlloc alloc) {
        return alloc;
    }

    @Override
    public Instr visit(LAlloc alloc) {
        return alloc;
    }

    @Override
    public Instr visit(BiExp exp) {
        Val val = marker.mark(exp);
        Var var = exp.var();
        if (valTab.contains(val)) {
            int id = valTab.get(val);
            valTab.hold(var, id);
            return new Def(var, valTab.belong(id));
        } else {
            int id = valTab.alloc(val, var);
            valTab.hold(var, id);
            Val.BiVal biVal = (Val.BiVal) val;
            return new BiExp(var, exp.op(),
                    valTab.belong(biVal.lhs()),
                    valTab.belong(biVal.rhs()));
        }
    }

    @Override
    public Instr visit(UnExp exp) {
        Val val = marker.mark(exp);
        Var var = exp.var();
        if (valTab.contains(val)) {
            int id = valTab.get(val);
            valTab.hold(var, id);
            return new Def(var, valTab.belong(id));
        } else {
            int id = valTab.alloc(val, var);
            valTab.hold(var, id);
            Val.UnVal unVal = (Val.UnVal) val;
            return new UnExp(var, exp.op(),
                    valTab.belong(unVal.sub()));
        }
    }

    @Override
    public Instr visit(Call call) {
        return new Call(
                call.var(),
                call.fun(),
                call.args().stream()
                        .map(marker::from)
                        .map(valTab::belong)
                        .collect(Collectors.toList()));
    }

    @Override
    public Instr visit(Def def) {
        int sub = marker.from(def.rhs());
        valTab.hold(def.var(), sub);
        return new Def(def.var(), valTab.belong(sub));
    }

    @Override
    public Instr visit(Load load) {
        Val val = marker.mark(load);
        Var var = load.var();
        int id = valTab.alloc(val, var);
        valTab.hold(var, id);
        Val.LdVal ldVal = (Val.LdVal) val;
        return new Load(var, valTab.belong(ldVal.loc()));
    }

    @Override
    public Instr visit(Store store) {
        int sub = marker.from(store.rhs());
        int lhs = marker.from(store.var());
        return new Store(
                (Var) valTab.belong(lhs),
                valTab.belong(sub));
    }

    @Override
    public Instr visit(Ret ret) {
        return ret;
    }

    @Override
    public Instr visit(Label label) {
        return label;
    }

    @Override
    public Instr visit(Br br) {
        return br;
    }

    @Override
    public Instr visit(Jmp jmp) {
        return jmp;
    }
}
