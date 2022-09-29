package org.jjppp.tools.symtab;

import org.jjppp.tools.parse.ParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SymTab {
    private static final List<Map<String, SymEntry>> mapStack = new ArrayList<>();

    private static Map<String, SymEntry> top = new HashMap<>();

    public static void init() {
    }

    public static void push() {
        mapStack.add(top);
        top = new HashMap<>();
        System.out.println("(");
    }

    public static void pop() {
        top = mapStack.remove(mapStack.size() - 1);
    }

    public static void add(String symbol, SymEntry entry) {
        top.put(symbol, entry);
    }

    public static SymEntry get(String symbol) {
        if (top.get(symbol) != null) {
            return top.get(symbol);
        }
        for (int i = mapStack.size() - 1; i >= 0; --i) {
            SymEntry result = mapStack.get(i).get(symbol);
            if (result != null) {
                return result;
            }
        }
        throw new ParserException("undefined symbol " + symbol);
    }
}
