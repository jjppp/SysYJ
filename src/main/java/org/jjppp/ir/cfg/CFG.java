package org.jjppp.ir.cfg;

import org.jjppp.ir.Fun;

import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public final class CFG {
    private final Fun fun;
    private final Map<Block, Node> blockNodeMap = new HashMap<>();
    private final Set<Edge> edges = new HashSet<>();

    public CFG(Fun fun) {
        this.fun = fun;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("digraph \"" + fun.name() + "\" {\n");
        builder.append("labeljust=l\n");
        HashMap<Node, Integer> map = new HashMap<>();
        int cnt = 0;
        for (var node : nodes()) {
            map.put(node, cnt);
            builder.append(cnt)
                    .append(" [shape=rectangle, label=\"")
                    .append(node.block().toString())
                    .append("\"];\n");
            cnt += 1;
        }
        for (var edge : edges()) {
            Node from = edge.from();
            Node to = edge.to();
            builder.append(map.get(from))
                    .append(" -> ")
                    .append(map.get(to))
                    .append("[label=")
                    .append(edge.type())
                    .append("];\n");
        }
        return builder.append("}").toString();
    }

    public Set<Node> nodes() {
        return blockNodeMap.keySet().stream()
                .map(this::fromBlock)
                .collect(Collectors.toSet());
    }

    public Set<Edge> edges() {
        return edges;
    }

    public Node fromBlock(Block block) {
        return blockNodeMap.get(block);
    }

    public void addBlock(Block block) {
        System.out.println("===\n" + block.getInstrs());
        Node node = new Node(block);
        nodes().add(node);
        blockNodeMap.put(block, node);
    }

    public void addEdge(Block from, Block to, Edge.EDGE_TYPE type) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Node fromNode = fromBlock(from);
        Node toNode = fromBlock(to);
        edges().add(new Edge(fromNode, toNode, type));
        fromNode.addSucc(toNode);
        toNode.addPred(fromNode);
    }

    public void toFolder(String folderPath) {
        try (var outputStream = new FileOutputStream(folderPath + fun.name() + ".dot")) {
            outputStream.write(this.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
