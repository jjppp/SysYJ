package org.jjppp.tools.parse;

import org.jjppp.ast.Item;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.stmt.Assign;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Val;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.ArrType;
import org.jjppp.type.BaseType;

import java.util.*;
import java.util.stream.Collectors;

public final class DefParser extends DefaultVisitor<List<Item>> {
    private final BaseType type;

    private DefParser(BaseType type) {
        this.type = type;
    }

    public static List<Item> parse(SysYParser.DefContext ctx, BaseType type) {
        return ctx.accept(new DefParser(type));
    }

    @Override
    public List<Item> visitDef(SysYParser.DefContext ctx) {
        Objects.requireNonNull(ctx.exp());
        String name = ctx.ID().getText();

        Exp defValExp = Optional.ofNullable(ctx.initVal())
                .map(ExpParser::parse)
                .orElse(null);
        if (ctx.exp().isEmpty()) { // var
            VarDecl varDecl = VarDecl.of(name, type);
            if (varDecl.isConst()) {
                Val defVal = Optional.ofNullable(defValExp)
                        .map(Exp::constEval)
                        .orElse(null);
                SymTab.add(varDecl, defVal);
                return Collections.emptyList();
            } else {
                if (SymTab.isGlobal()) { // global
                    if (defValExp == null) {
                        SymTab.add(varDecl, null);
                        return new ArrayList<>(List.of(varDecl));
                    }
                    throw new AssertionError("TODO");
                } else { // local
                    List<Item> result = new ArrayList<>(List.of(varDecl));
                    SymTab.add(varDecl, null);
                    if (defValExp != null) {
                        // type id = exp; => type id; id = exp;
                        result.add(Assign.of(varDecl, defValExp));
                    }
                    return result;
                }
            }
        } else { // arr
            List<Integer> widths = ctx.exp().stream()
                    .map(ExpParser::parse)
                    .map(Exp::constEval)
                    .map(Val::toInt)
                    .collect(Collectors.toList());
            ArrDecl arrDecl = ArrDecl.of(name, ArrType.of(type, widths));
            if (arrDecl.isConst()) {
                Val defVal = Optional.ofNullable(defValExp)
                        .map(Exp::constEval)
                        .orElse(null);
                SymTab.add(arrDecl, defVal);
                return Collections.emptyList();
            } else {
                if (defValExp == null) {
                    SymTab.add(arrDecl, null);
                    return Collections.emptyList();
                }
                throw new AssertionError("TODO");
            }
        }
    }
}
