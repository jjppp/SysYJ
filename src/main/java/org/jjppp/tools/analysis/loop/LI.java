package org.jjppp.tools.analysis.loop;

import org.jjppp.ir.Fun;
import org.jjppp.ir.cfg.CFG;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class LI {
    private final CFG cfg;
    private final Map<CFG.Node, Set<CFG.Node>> doms;
    private Set<LoopSet> result;

    public LI(CFG cfg, Map<CFG.Node, Set<CFG.Node>> doms) {
        this.cfg = cfg;
        this.doms = doms;
    }

    public void toFolder(String folder) {
        Fun.Signature fun = cfg.fun();
        try (PrintStream printStream = new PrintStream(folder + fun.name() + ".txt")) {
            for (var loop : result) {
                loop.forEach(node -> printStream.println(node.id()));
                printStream.println();
            }
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    private void dfs(CFG.Node node, LoopSet loop) {
        if (loop.contains(node)) {
            return;
        }
        loop.add(node);
        for (var pred : node.getPred()) {
            dfs(pred, loop);
        }
    }

    public Set<LoopSet> find() {
        result = new HashSet<>();
        for (var edge : cfg.edges()) {
            CFG.Node from = edge.from();
            CFG.Node to = edge.to();
            if (doms.get(from).contains(to)) { // back edge
                LoopSet loop = new LoopSet(to);
                dfs(from, loop);
                result.add(loop);
            }
        }
        return result;
    }
}
