package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.ArrValExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.FunExp;
import org.jjppp.ast.stmt.Assign;
import org.jjppp.ast.stmt.Block;
import org.jjppp.ast.stmt.ExpStmt;
import org.jjppp.immutable.ImmutableStack;
import org.jjppp.runtime.ArrVal;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Val;
import org.jjppp.tools.parse.ParserException;

import java.util.*;

public final class SymTab {
    private static final List<Assign> initBlock = new ArrayList<>();
    private static ImmutableStack<Map<String, Entry>> mapStack = ImmutableStack.empty();
    private static Map<String, Entry> top = new HashMap<>();

    public static void init() {
        initBlock.clear();
        mapStack = ImmutableStack.empty();
        top.clear();
    }

    public static void push() {
        mapStack = mapStack.push(top);
        top = new HashMap<>();
    }

    public static void pop() {
        top = mapStack.top();
        mapStack = mapStack.pop();
    }

    public static Block getInitBlock() {
        Block block = Block.empty();
        initBlock.forEach(block::add);
        FunDecl main = (FunDecl) get("main").getDecl();
        block.add(ExpStmt.of(FunExp.of(main, Collections.emptyList())));
        return block;
    }

    public static void addFun(FunDecl funDecl) {
        top.put(funDecl.name(), SymEntry.from(funDecl));
    }

    public static void addConstVar(VarDecl varDecl, BaseVal defVal) {
        top.put(varDecl.name(), ConstSymEntry.from(varDecl, defVal));
    }

    public static void addVar(VarDecl varDecl, Exp defValExp) {
        if (isGlobal()) {
            if (defValExp != null) {
                initBlock.add(Assign.of(varDecl, defValExp));
            }
        }
        top.put(varDecl.name(), SymEntry.from(varDecl));
    }

    public static void addConstArr(ArrDecl arrDecl, ArrVal defVal) {
        top.put(arrDecl.name(), ConstSymEntry.from(arrDecl, defVal));
    }

    public static void addArr(ArrDecl arrDecl, ArrValExp defValExp) {
        if (isGlobal()) {
            if (defValExp != null) {
                initBlock.addAll(Assign.of(arrDecl, defValExp));
            }
        }
        top.put(arrDecl.name(), SymEntry.from(arrDecl));
    }

    public static Entry get(String symbol) {
        if (top.get(symbol) != null) {
            return top.get(symbol);
        }
        for (var map : mapStack) {
            Entry result = map.get(symbol);
            if (result != null) {
                return result;
            }
        }
        throw new ParserException("undefined symbol " + symbol);
    }

    public static Val getVal(String symbol) {
        if (get(symbol) instanceof ConstSymEntry constSymEntry) {
            return constSymEntry.defVal().orElseThrow(() -> new RuntimeException("const symbol has no defVal"));
        }
        throw new ParserException("symbol " + symbol + " is not const");
    }

    public static boolean isGlobal() {
        return mapStack.isEmpty();
    }
}
