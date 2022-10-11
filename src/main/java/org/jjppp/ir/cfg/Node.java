package org.jjppp.ir.cfg;

import java.util.HashSet;
import java.util.Set;

public final class Node {
    private final Set<Node> succ = new HashSet<>();
    private final Set<Node> pred = new HashSet<>();
    private Block block;

    public Node(Block block) {
        this.block = block;
    }

    public Set<Node> getSucc() {
        return succ;
    }

    public Set<Node> getPred() {
        return pred;
    }

    public void addSucc(Node succ) {
        this.succ.add(succ);
    }

    public void addPred(Node pred) {
        this.pred.add(pred);
    }

    public Block block() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
