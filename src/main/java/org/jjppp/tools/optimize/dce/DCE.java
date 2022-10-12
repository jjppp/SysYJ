package org.jjppp.tools.optimize.dce;

import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;

import java.util.Set;
import java.util.stream.Collectors;

public final class DCE {
    private final CFG cfg;

    public DCE(CFG cfg) {
        this.cfg = cfg;
    }

    public Block doDCE(Block block) {
        Set<Var> useSet = cfg.useSet();
        return new Block(block.instrList().stream()
                .filter(instr -> useSet.contains(instr.var()) || instr.hasEffect())
                .collect(Collectors.toList()));
    }
}
