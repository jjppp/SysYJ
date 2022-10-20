package org.jjppp.tools.analysis.dataflow.dom;

import org.jjppp.ir.cfg.CFG;
import org.jjppp.tools.analysis.dataflow.AbsData;

import java.util.HashSet;
import java.util.Set;

public record DOMData(Set<CFG.Node> nodeSet) implements AbsData<DOMData> {
    public DOMData(Set<CFG.Node> nodeSet) {
        this.nodeSet = new HashSet<>(nodeSet);
    }

    @Override
    public DOMData merge(DOMData rhs) {
        Set<CFG.Node> result = new HashSet<>(nodeSet);
        result.retainAll(rhs.nodeSet);
        return new DOMData(result);
    }
}
