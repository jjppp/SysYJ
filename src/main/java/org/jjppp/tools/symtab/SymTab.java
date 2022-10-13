package org.jjppp.tools.symtab;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.ArrValExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.FunExp;
import org.jjppp.ast.stmt.Assign;
import org.jjppp.ast.stmt.Return;
import org.jjppp.ast.stmt.Scope;
import org.jjppp.immutable.ImmutableStack;
import org.jjppp.runtime.ArrVal;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Val;
import org.jjppp.tools.parse.ParserException;
import org.jjppp.type.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SymTab {
    private final static Set<FunDecl> LIB_FUNCTIONS = Stream.of(
            FunDecl.of("getint", FunType.of(IntType.ofNonConst(), Collections.emptyList()), null, null),
            FunDecl.of("getch", FunType.of(IntType.ofNonConst(), Collections.emptyList()), null, null),
            FunDecl.of("getfloat", FunType.of(FloatType.ofNonConst(), Collections.emptyList()), null, null),
            FunDecl.of("getarray", FunType.of(IntType.ofNonConst(), List.of(ArrType.of(IntType.ofNonConst(), List.of(Integer.MAX_VALUE)))), null, null),
            FunDecl.of("getfarray", FunType.of(IntType.ofNonConst(), List.of(ArrType.of(FloatType.ofNonConst(), List.of(Integer.MAX_VALUE)))), null, null),
            FunDecl.of("putint", FunType.of(VoidType.getInstance(), List.of(IntType.ofNonConst())), null, null),
            FunDecl.of("putch", FunType.of(VoidType.getInstance(), List.of(IntType.ofNonConst())), null, null),
            FunDecl.of("putfloat", FunType.of(VoidType.getInstance(), List.of(FloatType.ofNonConst())), null, null),
            FunDecl.of("putarray", FunType.of(VoidType.getInstance(), List.of(IntType.ofNonConst(), ArrType.of(IntType.ofNonConst(), List.of(Integer.MAX_VALUE)))), null, null),
            FunDecl.of("putfarray", FunType.of(VoidType.getInstance(), List.of(IntType.ofNonConst(), ArrType.of(FloatType.ofNonConst(), List.of(Integer.MAX_VALUE)))), null, null)
    ).collect(Collectors.toSet());
    private static SymTab INSTANCE = new SymTab();
    private final List<Assign> initBlock = new ArrayList<>();
    private ImmutableStack<Map<String, Entry>> mapStack = ImmutableStack.empty();
    private Map<String, Entry> top = new HashMap<>();

    private SymTab() {
        LIB_FUNCTIONS.forEach(
                funDecl -> top.put(funDecl.name(), SymEntry.from(funDecl, true))
        );
    }

    public static SymTab getInstance() {
        return INSTANCE;
    }

    public static void init() {
        INSTANCE = new SymTab();
    }

    public void push() {
        mapStack = mapStack.push(top);
        top = new HashMap<>();
    }

    public void pop() {
        top = mapStack.top();
        mapStack = mapStack.pop();
    }

    public Scope getInitBlock() {
        Scope scope = Scope.empty();
        initBlock.forEach(scope::add);
        FunDecl main = (FunDecl) get("main").getDecl();
        scope.add(Return.of(FunExp.of(main, Collections.emptyList())));
        return scope;
    }

    public void addFun(FunDecl funDecl) {
        top.put(funDecl.name(), SymEntry.from(funDecl));
    }

    public void addConstVar(VarDecl varDecl, BaseVal defVal) {
        top.put(varDecl.name(), ConstSymEntry.from(varDecl, defVal));
    }

    public void addVar(VarDecl varDecl, Exp defValExp) {
        if (isGlobal()) {
            if (defValExp != null) {
                initBlock.add(Assign.of(varDecl, defValExp));
            }
        }
        top.put(varDecl.name(), SymEntry.from(varDecl));
    }

    public void addConstArr(ArrDecl arrDecl, ArrVal defVal) {
        top.put(arrDecl.name(), ConstSymEntry.from(arrDecl, defVal));
    }

    public void addArr(ArrDecl arrDecl, ArrValExp defValExp) {
        if (isGlobal()) {
            if (defValExp != null) {
                initBlock.addAll(Assign.of(arrDecl, defValExp));
            }
        }
        top.put(arrDecl.name(), SymEntry.from(arrDecl));
    }

    public Entry get(String symbol) {
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

    public Val getVal(String symbol) {
        if (get(symbol) instanceof ConstSymEntry constSymEntry) {
            return constSymEntry.defVal().orElseThrow(() -> new RuntimeException("const symbol has no defVal"));
        }
        throw new ParserException("symbol " + symbol + " is not const");
    }

    public boolean isGlobal() {
        return mapStack.isEmpty();
    }
}
