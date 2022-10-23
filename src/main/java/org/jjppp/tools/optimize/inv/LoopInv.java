package org.jjppp.tools.optimize.inv;

import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.instr.*;
import org.jjppp.runtime.Val;
import org.jjppp.tools.analysis.dataflow.reach.RDData;
import org.jjppp.tools.analysis.loop.LoopSet;

public final class LoopInv implements InstrVisitor<Boolean> {
    private final CFG.Node node;
    private final LoopSet loop;
    private final RDData reach;

    public LoopInv(CFG.Node node, LoopSet loop, RDData reach) {
        this.node = node;
        this.loop = loop;
        this.reach = new RDData(reach);
    }

    private void transfer(Instr instr) {
        Var var = instr.var();
        if (var != null) {
            reach.clear(var);
            reach.put(var, instr);
        }
    }

    public void mark() {
        for (var instr : node.block()) {
            instr.setInv(isInv(instr));
            transfer(instr);
        }
        /* TODO: BUG FIX
        List<Instr> invs = node.block().instrList().stream().filter(Instr::isInv).toList();
        node.block().removeInv();
        for (int i = invs.size() - 1; i >= 0; --i) {
            Instr instr = invs.get(i);
            CFG.Node preHeader = loop.getHeader().getPred().stream()
                    .filter(x -> !loop.contains(x))
                    .findAny().orElseThrow();
            preHeader.block().prepend(instr);
        }*/
    }

    private boolean isInv(Instr instr) {
        return instr.accept(this);
    }

    private boolean isInv(Ope ope) {
        if (ope instanceof Val) {
            return true;
        } else if (ope instanceof Var var) {
            return reach.get(var).stream()
                    .map(Instr::belongTo)
                    .map(Block::belongTo)
                    .noneMatch(loop::contains);
        }
        throw new AssertionError("TODO");
    }

    @Override
    public Boolean visitDefault(Instr ignore) {
        return false;
    }

    @Override
    public Boolean visit(BiExp exp) {
        return isInv(exp.lhs()) && isInv(exp.rhs());
    }

    @Override
    public Boolean visit(UnExp exp) {
        return isInv(exp.sub());
    }

    @Override
    public Boolean visit(Ass ass) {
        return isInv(ass.rhs());
    }
}
