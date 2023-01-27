package org.jjppp.tools.analysis.dataflow.cp;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.instr.*;
import org.jjppp.ir.instr.control.Br;
import org.jjppp.ir.instr.control.Jmp;
import org.jjppp.ir.instr.control.Label;
import org.jjppp.ir.instr.control.Ret;
import org.jjppp.ir.instr.memory.GAlloc;
import org.jjppp.ir.instr.memory.LAlloc;
import org.jjppp.ir.instr.memory.Load;
import org.jjppp.ir.instr.memory.Store;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Int;
import org.jjppp.tools.analysis.dataflow.DFA;

import java.util.*;

import static org.jjppp.tools.analysis.dataflow.cp.CPData.CPFact.*;

public final class CP extends DFA<CPData> implements InstrVisitor<CPData> {
    private CPData dataIn;

    public CP(CFG cfg) {
        super(cfg);
        init();
    }

    private CPData.CPFact fromOpe(Ope ope) {
        if (ope instanceof BaseVal val) {
            return from(val);
        } else {
            return dataIn.get((Var) ope);
        }
    }

    public CFG doCP() {
        var result = solve();
        for (var node : cfg.nodes()) {
            this.dataIn = new CPData(result.get(node).varFactMap());
            List<Instr> instrList = new ArrayList<>();
            for (var instr : node.block().instrList()) {
                instrList.add(instr.accept(new InstrVisitor<>() {
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
                        Var var = exp.var();
                        CPData.CPFact lhsFact = fromOpe(exp.lhs());
                        CPData.CPFact rhsFact = fromOpe(exp.rhs());
                        Ope lhs = lhsFact.isConst() ? lhsFact.val() : exp.lhs();
                        Ope rhs = rhsFact.isConst() ? rhsFact.val() : exp.rhs();
                        return new BiExp(var, exp.op(), lhs, rhs);
                    }

                    @Override
                    public Instr visit(UnExp exp) {
                        CPData.CPFact subFact = fromOpe(exp.sub());
                        return subFact.isConst() ? Ass.of(exp.var(), (BaseVal) exp.op().apply(subFact.val())) : exp;
                    }

                    @Override
                    public Instr visit(Call call) {
                        return call;
                    }

                    @Override
                    public Instr visit(Ass ass) {
                        CPData.CPFact rhsFact = fromOpe(ass.rhs());
                        return rhsFact.isConst() ? Ass.of(ass.var(), rhsFact.val()) : ass;
                    }

                    @Override
                    public Instr visit(Load load) {
                        return load;
                    }

                    @Override
                    public Instr visit(Store store) {
                        CPData.CPFact rhsFact = fromOpe(store.rhs());
                        return rhsFact.isConst() ? new Store(store.var(), rhsFact.val()) : store;
                    }

                    @Override
                    public Instr visit(Ret ret) {
                        CPData.CPFact retFact = fromOpe(ret.retVal());
                        return retFact.isConst() ? new Ret(retFact.val()) : ret;
                    }

                    @Override
                    public Instr visit(Label label) {
                        return label;
                    }

                    @Override
                    public Instr visit(Br br) {
                        CPData.CPFact condFact = fromOpe(br.cond());
                        if (condFact.isConst()) {
                            if (((Int) condFact.val()).value() == 0) {
                                return Jmp.of(br.sFls());
                            } else {
                                return Jmp.of(br.sTru());
                            }
                        }
                        return br;
                    }

                    @Override
                    public Instr visit(Jmp jmp) {
                        return jmp;
                    }

                    @Override
                    public Instr visit(LibCall call) {
                        return call;
                    }
                }));
                this.dataIn = instr.accept(this);
            }
            node.setBlock(new Block(instrList));
        }
        return cfg;
    }

    @Override
    protected CPData transfer(Instr instr, CPData dataIn) {
        this.dataIn = new CPData(dataIn.varFactMap());
        return instr.accept(this);
    }

    @Override
    protected void init() {
        List<Var> args = new ArrayList<>(cfg.fun().args());
        Set<Var> varSet = new HashSet<>(cfg.defVars());

        Map<Var, CPData.CPFact> emptyMap = new HashMap<>();
        varSet.forEach(var -> emptyMap.put(var, CPData.CPFact.UNDEF));
        cfg.nodes().forEach(node -> inMap.put(node, new CPData(emptyMap)));
        cfg.nodes().forEach(node -> outMap.put(node, new CPData(emptyMap)));

        args.forEach(var -> emptyMap.put(var, NAC));
        inMap.put(cfg.entry(), new CPData(emptyMap));
        varSet.addAll(args);
    }

    @Override
    public CPData visit(GAlloc alloc) {
        dataIn.put(alloc.var(), NAC);
        return dataIn;
    }

    @Override
    public CPData visit(LAlloc alloc) {
        dataIn.put(alloc.var(), NAC);
        return dataIn;
    }

    @Override
    public CPData visit(BiExp exp) {
        Var var = exp.var();
        CPData.CPFact lhsFact = fromOpe(exp.lhs());
        CPData.CPFact rhsFact = fromOpe(exp.rhs());
        if (lhsFact == NAC || rhsFact == NAC) {
            dataIn.put(var, NAC);
        } else if (lhsFact == UNDEF) {
            dataIn.put(var, rhsFact);
        } else if (rhsFact == UNDEF) {
            dataIn.put(var, lhsFact);
        } else {
            dataIn.put(var, from(exp.op().apply(lhsFact.val(), rhsFact.val())));
        }
        return dataIn;
    }

    @Override
    public CPData visit(UnExp exp) {
        CPData.CPFact subFact = fromOpe(exp.sub());
        if (subFact == NAC) {
            dataIn.put(exp.var(), NAC);
        } else if (subFact == UNDEF) {
            dataIn.put(exp.var(), UNDEF);
        } else {
            dataIn.put(exp.var(), from((BaseVal) exp.op().apply(subFact.val())));
        }
        return dataIn;
    }

    @Override
    public CPData visit(Call call) {
        dataIn.put(call.var(), NAC);
        return dataIn;
    }

    @Override
    public CPData visit(Ass ass) {
        if (ass.rhs() instanceof Var var) {
            dataIn.put(ass.var(), dataIn.get(var));
        } else if (ass.rhs() instanceof BaseVal val) {
            dataIn.put(ass.var(), CPData.CPFact.from(val));
        }
        return dataIn;
    }

    @Override
    public CPData visit(Load load) {
        dataIn.put(load.var(), NAC);
        return dataIn;
    }

    @Override
    public CPData visit(Store store) {
        return dataIn;
    }

    @Override
    public CPData visit(Ret ret) {
        return dataIn;
    }

    @Override
    public CPData visit(Label label) {
        return dataIn;
    }

    @Override
    public CPData visit(Br br) {
        return dataIn;
    }

    @Override
    public CPData visit(Jmp jmp) {
        return dataIn;
    }

    @Override
    public CPData visit(LibCall call) {
        dataIn.put(call.var(), NAC);
        return dataIn;
    }
}
