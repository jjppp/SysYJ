package org.jjppp.ir.cfg;

import org.jjppp.ir.Instr;
import org.jjppp.ir.control.Br;
import org.jjppp.ir.control.Jmp;
import org.jjppp.ir.control.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CFGBuilder {
    public static CFG buildFrom(List<Instr> instrList) {
        Map<Label, Block> labelBlockMap = new HashMap<>();
        Block block = Block.empty();
        CFG cfg = new CFG();
        Block last = null;

        for (Instr instr : instrList) {
            if (instr instanceof Jmp jmp) {
                block.add(jmp);
                cfg.addBlock(block);
                if (last != null) {
                    cfg.addEdge(last, block, Edge.EDGE_TYPE.FALL_THROUGH);
                    last = null;
                }
                block = Block.empty();
            } else if (instr instanceof Br br) {
                block.add(br);
                cfg.addBlock(block);
                if (last != null) {
                    cfg.addEdge(last, block, Edge.EDGE_TYPE.FALL_THROUGH);
                    last = null;
                }
                block = Block.empty();
            } else if (instr instanceof Label label) {
                if (block.size() != 0) {
                    cfg.addBlock(block);
                    if (last != null) {
                        cfg.addEdge(last, block, Edge.EDGE_TYPE.FALL_THROUGH);
                    }
                    last = block;
                }
                block = Block.of(label);
                labelBlockMap.put(label, block);
            } else {
                block.add(instr);
            }
        }

        if (block.size() != 0) {
            cfg.addBlock(block);
        }

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
