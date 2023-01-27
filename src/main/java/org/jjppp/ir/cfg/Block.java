package org.jjppp.ir.cfg;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Def;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.control.Br;
import org.jjppp.ir.instr.control.Jmp;

import java.util.*;
import java.util.stream.Collectors;

public final class Block implements Iterable<Instr> {
    private final List<Instr> instrList;
    private CFG.Node belongTo;

    public Block(List<Instr> instrList) {
        this.instrList = new ArrayList<>(instrList);
        instrList.forEach(instr -> instr.setBelongTo(this));
    }

    public static Block empty() {
        return new Block(new ArrayList<>());
    }

    public static Block of(Instr instr) {
        return new Block(List.of(instr));
    }

    public List<Instr> instrList() {
        return instrList;
    }

    public CFG.Node belongTo() {
        return belongTo;
    }

    public void setBelongTo(CFG.Node belongTo) {
        this.belongTo = Objects.requireNonNull(belongTo);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof Block block) {
            return instrList().equals(block.instrList());
        }
        return false;
    }

    public void prepend(Instr instr) {
        if (lastInstr() instanceof Br || lastInstr() instanceof Jmp) {
            instrList.add(0, instr);
        } else {
            instrList.add(instr);
        }
        instr.setBelongTo(this);
    }

    public void add(Instr instr) {
        instrList.add(instr);
        instr.setBelongTo(this);
    }

    public void removeInv() {
        List<Instr> invs = instrList().stream()
                .filter(Instr::isInv).toList();
        invs.forEach(instr -> {
            instrList().remove(instr);
            instr.setInv(false);
        });
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

    public Set<Var> defVars() {
        return instrList.stream()
                .filter(Def.class::isInstance)
                .map(Def.class::cast)
                .map(Def::var)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("instr count: ").append(size()).append("\\n");
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
