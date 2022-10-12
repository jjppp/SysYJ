package org.jjppp.ir.cfg;

import org.jjppp.ir.Instr;
import org.jjppp.ir.Var;

import java.util.*;
import java.util.stream.Collectors;

public final class Block implements Iterable<Instr> {
    private List<Instr> instrList;

    public Block(List<Instr> instrList) {
        this.instrList = new ArrayList<>(instrList);
    }

    public static Block empty() {
        return new Block(new ArrayList<>());
    }

    public static Block of(Instr instr) {
        return new Block(List.of(instr));
    }

    public void add(Instr instr) {
        instrList.add(instr);
    }

    public Instr lastInstr() {
        return instrList.get(instrList.size() - 1);
    }

    public Set<Var> useSet() {
        return instrList.stream()
                .map(Instr::useSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public void dce() {
        instrList = instrList.stream()
                .filter(x -> !x.dead())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("instr count: ").append(size()).append("\n");
        for (Instr instr : instrList) {
            builder.append(instr).append("\\n");
        }
        return builder.toString();
    }

    public boolean isEmpty() {
        return instrList.isEmpty();
    }

    public int size() {
        return instrList.size();
    }

    @Override
    public Iterator<Instr> iterator() {
        return instrList.listIterator();
    }
}
