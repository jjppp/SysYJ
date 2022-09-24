package org.jjppp.tools.symtab;

import org.jjppp.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SymTab {
    private final Map<Symbol, SymEntry> map = new HashMap<>();

    public void add(Symbol symbol, int addr, Type type) {
        map.put(symbol, new SymEntry(symbol, addr, type));
    }

    public Optional<SymEntry> get(Symbol symbol) {
        return Optional.ofNullable(map.get(symbol));
    }
}
