package org.jjppp.tools.optimize.dce;

import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.cfg.CFG.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public final class DCE {
    private static void delUnreachableBlock(CFG cfg) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.add(cfg.entry());
        while (!queue.isEmpty()) {
            Node front = queue.poll();
            if (!visited.contains(front)) {
                visited.add(front);
                queue.addAll(front.getSucc());
            }
        }
        for (Node node : cfg.nodes()) {
            if (!visited.contains(node)) {
                cfg.removeBlock(node.block());
            }
        }
    }

    private static Block doDCE(CFG cfg, Block block) {
        Set<Var> useSet = cfg.useSet();
        return new Block(block.instrList().stream()
                .filter(instr -> useSet.contains(instr.var()) || instr.hasEffect())
                .collect(Collectors.toList()));
    }

    public static void doDCE(CFG cfg) {
        delUnreachableBlock(cfg);
        cfg.nodes().forEach(x -> x.setBlock(doDCE(cfg, x.block())));
    }
}
