package org.jjppp.ir.cfg;

import org.jjppp.ir.Fun;
import org.jjppp.ir.cfg.CFG.Node;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.control.Br;
import org.jjppp.ir.instr.control.Jmp;
import org.jjppp.ir.instr.control.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CFGBuilder {
    private final List<Instr> instrList;
    private final Map<Label, Block> labelBlockMap;
    private final CFG cfg;

    public CFGBuilder(Fun fun) {
        instrList = fun.body();
        labelBlockMap = new HashMap<>();
        cfg = new CFG(fun.signature());
    }

    private List<Block> grouping() {
        List<Block> blockList = new ArrayList<>();
        Block block = Block.empty();
        for (Instr instr : instrList) {
            if (instr instanceof Label label) {
                if (!block.isEmpty()) {
                    blockList.add(block);
                }
                block = Block.of(label);
                labelBlockMap.put(label, block);
            } else {
                block.add(instr);
                if (instr instanceof Jmp) {
                    blockList.add(block);
                    block = Block.empty();
                } else if (instr instanceof Br) {
                    blockList.add(block);
                    block = Block.empty();
                }
            }
        }

        if (!block.isEmpty()) {
            blockList.add(block);
        }
        return blockList;
    }

    public CFG build() {
        Block last = null;
        List<Block> blockList = grouping();

        for (Block block : blockList) {
            cfg.addBlock(block);
            if (last != null) {
                cfg.addEdge(last, block, Edge.EDGE_TYPE.FALL_THROUGH);
                last = null;
            }
            if (!(block.lastInstr() instanceof Jmp
                    || block.lastInstr() instanceof Br)) {
                last = block;
            }
        }
        cfg.setEntry(blockList.get(0));
        cfg.setExit(blockList.get(blockList.size() - 1));

        for (Node node : cfg.nodes()) {
            Block from = node.block();
            Instr instr = from.lastInstr();
            if (instr instanceof Jmp jmp) {
                Label label = jmp.target();
                Block target = labelBlockMap.get(label);
                cfg.addEdge(from, target, Edge.EDGE_TYPE.JMP);
            } else if (instr instanceof Br br) {
                Label sTru = br.sTru();
                Label sFls = br.sFls();
                cfg.addEdge(from, labelBlockMap.get(sTru), Edge.EDGE_TYPE.TRUE);
                cfg.addEdge(from, labelBlockMap.get(sFls), Edge.EDGE_TYPE.FALSE);
            }
        }
        return cfg;
    }
}
