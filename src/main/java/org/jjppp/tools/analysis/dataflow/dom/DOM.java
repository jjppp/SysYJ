package org.jjppp.tools.analysis.dataflow.dom;

import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.instr.Instr;
import org.jjppp.tools.analysis.dataflow.DFA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DOM extends DFA<DOMData> {
    public DOM(CFG cfg) {
        super(cfg);
    }

    public Map<CFG.Node, Set<CFG.Node>> doDOM() {
        init();
        Map<CFG.Node, Set<CFG.Node>> result = new HashMap<>();
        var tmp = solve();
        for (var entry : tmp.entrySet()) {
            result.put(entry.getKey(), entry.getValue().nodeSet());
        }
        return result;
    }

    @Override
    protected void init() {
        Set<CFG.Node> initialSet = new HashSet<>(cfg.nodes());
        cfg.nodes().forEach(node -> inMap.put(node, new DOMData(initialSet)));
        cfg.nodes().forEach(node -> outMap.put(node, new DOMData(initialSet)));
        inMap.put(cfg.entry(), new DOMData(Stream.of(cfg.entry()).collect(Collectors.toSet())));
    }

    @Override
    protected DOMData transfer(CFG.Node node, DOMData dataIn) {
        DOMData data = new DOMData(dataIn.nodeSet());
        data.nodeSet().add(node);
        return data;
    }

    @Override
    protected DOMData transfer(Instr instr, DOMData dataIn) {
        throw new AssertionError("should not reach here");
    }
}
