package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.immutable.ImmutableStack;
import org.jjppp.runtime.Val;
import org.jjppp.tools.parse.ParserException;

import java.util.HashMap;
import java.util.Map;

public final class SymTab {
    private static ImmutableStack<Map<String, SymEntry>> mapStack = ImmutableStack.empty();

    private static Map<String, SymEntry> top = new HashMap<>();

    public static void init() {
    }

    public static void push() {
        mapStack = mapStack.push(top);
        top = new HashMap<>();
    }

    public static void pop() {
        top = mapStack.top();
        mapStack = mapStack.pop();
    }

    public static void add(FunDecl funDecl) {
        top.put(funDecl.name(), SymEntry.from(funDecl));
    }

    public static void add(Decl decl, Val defVal) {
        String name = decl.name();
        if (decl instanceof VarDecl varDecl) {
            top.put(name, SymEntry.from(varDecl, defVal));
        } else if (decl instanceof ArrDecl arrDecl) {
            top.put(name, SymEntry.from(arrDecl, defVal));
        } else {
            throw new ParserException("unknown declaration");
        }
    }

    public static SymEntry get(String symbol) {
        if (top.get(symbol) != null) {
            return top.get(symbol);
        }
        for (var map : mapStack) {
            SymEntry result = map.get(symbol);
            if (result != null) {
                return result;
            }
        }
        throw new ParserException("undefined symbol " + symbol);
    }

    public static Val getVal(String symbol) {
        SymEntry entry = get(symbol);
        if (entry.isConst()) {
            return entry.defVal().orElseThrow(() -> new RuntimeException("const symbol has no defVal"));
        }
        throw new ParserException("symbol " + symbol + " is not const");
    }

    public static boolean isGlobal() {
        return mapStack.isEmpty();
    }
}
