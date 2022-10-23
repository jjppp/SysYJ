package org.jjppp.tools.analysis.dataflow.reach;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Instr;
import org.jjppp.tools.analysis.dataflow.AbsData;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RDData implements AbsData<RDData>, Iterable<Instr> {
    private final Map<Var, DefSet> varDefMap;

    public RDData() {
        varDefMap = new HashMap<>();
    }

    public RDData(Map<Var, DefSet> varDefMap) {
        this.varDefMap = varDefMap;
    }

    public RDData(RDData rhs) {
        varDefMap = new HashMap<>(rhs.varDefMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof RDData rdData) {
            return Objects.equals(varDefMap, rdData.varDefMap);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return varDefMap.hashCode();
    }

    public DefSet get(Var var) {
        return varDefMap.getOrDefault(var, DefSet.empty());
    }

    public void clear(Var var) {
        varDefMap.put(var, DefSet.empty());
    }

    public void put(Var var, Instr instr) {
        varDefMap.putIfAbsent(var, DefSet.empty());
        varDefMap.get(var).add(instr);
    }

    @Override
    public RDData merge(RDData rhs) {
        Map<Var, DefSet> result = new HashMap<>(varDefMap);
        for (Var var : rhs.varDefMap.keySet()) {
            DefSet defSet = result.getOrDefault(var, DefSet.empty());
            result.put(var, DefSet.merge(defSet, rhs.varDefMap.get(var)));
        }
        return new RDData(result);
    }

    @Override
    public Iterator<Instr> iterator() {
        return varDefMap.values().stream()
                .flatMap(DefSet::stream)
                .iterator();
    }

    public record DefSet(Set<Instr> defSet) implements Iterable<Instr> {
        public static DefSet empty() {
            return new DefSet(new HashSet<>());
        }

        public static DefSet merge(DefSet lhs, DefSet rhs) {
            return new DefSet(Stream.concat(lhs.defSet().stream(), rhs.defSet().stream())
                    .collect(Collectors.toSet()));
        }

        public void add(Instr instr) {
            defSet.add(instr);
        }

        public boolean isEmpty() {
            return defSet.isEmpty();
        }

        public Stream<Instr> stream() {
            return defSet().stream();
        }

        @Override
        public Iterator<Instr> iterator() {
            return defSet.iterator();
        }
    }
}
