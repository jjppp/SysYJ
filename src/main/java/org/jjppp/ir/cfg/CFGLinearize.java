package org.jjppp.ir.cfg;

import org.jjppp.ir.Fun;

import java.util.HashSet;
import java.util.Set;

public final class CFGLinearize {
    private final CFG cfg;
    private final Set<CFG.Node> visited = new HashSet<>();
    private final Block block;

    public CFGLinearize(CFG cfg) {
        this.cfg = cfg;
        block = Block.empty();
        dfs1(cfg.entry());
        visited.add(cfg.exit());
    }

    private void dfs1(CFG.Node node) {
        visited.add(node);
        node.block().forEach(block::add);
        for (var edge : node.outEdges()) {
            var succ = edge.to();
            if (edge.type().equals(Edge.EDGE_TYPE.FALL_THROUGH)
                    && !visited.contains(succ)) {
                dfs1(succ);
                return;
            }
        }
    }

    private void dfs2(CFG.Node node) {
        visited.add(node);
        node.block().forEach(block::add);
        for (var edge : node.outEdges()) {
            var succ = edge.to();
            if (!visited.contains(succ)) {
                dfs1(succ);
                return;
            }
        }
    }

    public Fun toFun() {
        Fun.Signature signature = cfg.fun();
        for (var node : cfg.nodes()) {
            if (node.isLead() && !visited.contains(node)) {
                dfs1(node);
            }
        }
        for (var node : cfg.nodes()) {
            if (!visited.contains(node)) {
                dfs2(node);
            }
        }
        cfg.exit().block().forEach(block::add);
        return new Fun(signature, block.instrList());
    }
}
