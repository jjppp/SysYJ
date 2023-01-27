package org.jjppp.tools.analysis.loop;

import org.jjppp.ir.cfg.CFG;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<CFG.Node> getExits() {
        return nodeSet.stream()
                .filter(node -> node.getSucc().stream().anyMatch(x -> !contains(x)))
                .collect(Collectors.toSet());
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
