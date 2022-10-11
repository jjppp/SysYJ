package org.jjppp.tools.optimize;

import org.jjppp.ir.Def;
import org.jjppp.ir.Exp;
import org.jjppp.ir.Instr;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.mem.Alloc;

import static org.jjppp.tools.optimize.ValTab.Val;

public final class LVN {
    private final ValTab valTab = new ValTab();

    public void doLVN(Block input) {
        for (Instr instr : input) {
            System.out.print(instr + " -> ");
            if (instr instanceof Def def) {
                if (def.rhs() instanceof Alloc) {
                } else if (def.rhs() instanceof Exp.Call) {
                } else if (def.rhs() instanceof Exp.Load) {
                    Val val = valTab.from(def.rhs());
                    int id = valTab.alloc(val, def.var());
                    valTab.hold(def.var(), id);
                    Val.LdVal ldVal = (Val.LdVal) val;
                    def.setRhs(new Exp.Load(valTab.belong(ldVal.loc())));
                } else if (def.rhs().isMove()) {
                    Val.UnVal val = (Val.UnVal) valTab.from(def.rhs());
                    valTab.hold(def.var(), val.sub());
                    def.setRhs(valTab.belong(val.sub()));
                } else {
                    Val val = valTab.from(def.rhs());

                    if (valTab.contains(val)) {
                        int id = valTab.get(val);
                        valTab.hold(def.var(), id);
                        def.setRhs(valTab.belong(id));
                    } else {
                        int id = valTab.alloc(val, def.var());
                        valTab.hold(def.var(), id);
                        if (def.rhs() instanceof Exp.BiExp biExp) {
                            Val.BiVal biVal = (Val.BiVal) val;
                            def.setRhs(new Exp.BiExp(biExp.op(),
                                    valTab.belong(biVal.lhs()),
                                    valTab.belong(biVal.rhs())));
                        } else if (def.rhs() instanceof Exp.UnExp unExp) {
                            Val.UnVal unVal = (Val.UnVal) val;
                            def.setRhs(new Exp.UnExp(unExp.op(),
                                    valTab.belong(unVal.sub())));
                        }
                    }
                }
            }
            System.out.println(instr);
        }
    }
}
