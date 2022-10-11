package org.jjppp.tools.optimize;

import org.jjppp.ir.Def;
import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.cfg.Node;

import java.util.Set;

public final class DCE {
    public void doDCE(CFG cfg) {
        Set<Var> useSet = cfg.useSet();
        for (var node : cfg.nodes()) {
            var block = node.block();
            for (var instr : block) {
                if (instr instanceof Def def) {
                    Var var = def.var();
                    if (useSet.stream().noneMatch(var::equals)) {
                        instr.setDead();
                    }
                }
            }
        }
        cfg.nodes().stream()
                .map(Node::block)
                .forEach(Block::dce);
    }
}
