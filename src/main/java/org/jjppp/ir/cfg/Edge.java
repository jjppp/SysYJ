package org.jjppp.ir.cfg;

import org.jjppp.ir.cfg.CFG.Node;

public record Edge(Node from, Node to, EDGE_TYPE type) {
    public enum EDGE_TYPE {
        TRUE, FALSE, JMP, FALL_THROUGH
    }
}
