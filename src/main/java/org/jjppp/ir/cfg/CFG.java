package org.jjppp.ir.cfg;

import org.jjppp.ir.Fun;
import org.jjppp.ir.Var;

import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public final class CFG {
    private final Fun fun;
    private final Map<Block, Node> blockNodeMap = new HashMap<>();
    private final Map<Node, Map<Node, Edge>> edgeFrom = new HashMap<>();
    private Node entry = null;
    private Node exit = null;

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
            builder.append(cnt);
            if (node.equals(entry()) || node.equals(exit())) {
                builder.append(" [shape=rect, style=filled, fillcolor=gray, label=\"");
            } else {
                builder.append(" [shape=box, label=\"");
            }
            builder.append(node.block().toString())
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

    public void setEntry(Block entry) {
        this.entry = blockNodeMap.get(entry);
    }

    public void setExit(Block exit) {
        this.exit = blockNodeMap.get(exit);
    }

    public Node entry() {
        return Optional.ofNullable(entry).orElseThrow();
    }

    public Node exit() {
        return Optional.of(exit).orElseThrow();
    }

    public Set<Node> nodes() {
        return blockNodeMap.keySet().stream()
                .map(this::fromBlock)
                .collect(Collectors.toSet());
    }

    public Set<Edge> edges() {
        return edgeFrom.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Node fromBlock(Block block) {
        return blockNodeMap.get(block);
    }

    public Set<Var> useSet() {
        return nodes().stream()
                .map(Node::block)
                .map(Block::useSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public void addBlock(Block block) {
        Node node = new Node(block);
        nodes().add(node);
        blockNodeMap.put(block, node);
        edgeFrom.put(node, new HashMap<>());
    }

    public void removeBlock(Block block) {
        Node node = blockNodeMap.get(block);
        blockNodeMap.remove(block);
        edgeFrom.remove(node);
        for (var pred : node.getPred()) {
            edgeFrom.get(pred).remove(node);
        }
        node.getSucc().forEach(x -> x.removePred(node));
        node.getPred().forEach(x -> x.removeSucc(node));
    }

    public void addEdge(Block from, Block to, Edge.EDGE_TYPE type) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Node fromNode = fromBlock(from);
        Node toNode = fromBlock(to);
        edgeFrom.get(fromNode).put(toNode, new Edge(fromNode, toNode, type));
        fromNode.addSucc(toNode);
        toNode.addPred(fromNode);
    }

    public void toFolder(String folderPath) {
        try (var outputStream = new FileOutputStream(folderPath + fun.name() + ".dot")) {
            outputStream.write(this.toString().getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

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

        public void removeSucc(Node node) {
            succ.remove(node);
        }

        public Set<Node> getPred() {
            return pred;
        }

        public void removePred(Node node) {
            pred.remove(node);
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
            blockNodeMap.remove(this.block);
            this.block = block;
            blockNodeMap.put(block, this);
        }
    }

}
