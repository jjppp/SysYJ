package org.jjppp.tools.analysis.loop;

import org.jjppp.ir.cfg.CFG;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class LoopSet implements Iterable<CFG.Node> {
    private final Set<CFG.Node> nodeSet = new HashSet<>();
    private final CFG.Node header;

    public LoopSet(CFG.Node header) {
        this.header = header;
        nodeSet.add(header);
    }

    public CFG.Node getHeader() {
        return header;
    }

    public void add(CFG.Node node) {
        nodeSet.add(node);
    }

    public boolean contains(CFG.Node node) {
        return nodeSet.contains(node);
    }

    @Override
    public Iterator<CFG.Node> iterator() {
        return nodeSet.iterator();
    }
}
