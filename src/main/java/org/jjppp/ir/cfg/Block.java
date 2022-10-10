package org.jjppp.ir.cfg;

import org.jjppp.ir.Instr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Block implements Iterable<Instr> {
    private final ArrayList<Instr> instrList;

    public Block(List<Instr> instrList) {
        this.instrList = new ArrayList<>(instrList);
    }

    public static Block empty() {
        return new Block(new ArrayList<>());
    }

    public static Block of(Instr instr) {
        return new Block(List.of(instr));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Instr instr : instrList) {
            builder.append(instr).append("\\n");
        }
        return builder.toString();
    }

    public void add(Instr instr) {
        instrList.add(instr);
    }

    public Instr lastInstr() {
        return instrList.get(instrList.size() - 1);
    }

    public List<Instr> getInstrs() {
        return instrList;
    }

    public int size() {
        return instrList.size();
    }

    @Override
    public Iterator<Instr> iterator() {
        return instrList.listIterator();
    }
}
