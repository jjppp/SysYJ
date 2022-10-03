package org.jjppp.tools.parse;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.ArrValExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.ArrVal;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Val;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.ArrType;
import org.jjppp.type.BaseType;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GlobalDefParser extends DefaultVisitor<List<Decl>> {
    private final BaseType type;

    private GlobalDefParser(BaseType type) {
        this.type = type;
    }

    public static List<Decl> parse(SysYParser.DefContext ctx, BaseType type) {
        return ctx.accept(new GlobalDefParser(type));
    }

    @Override
    public List<Decl> visitDef(SysYParser.DefContext ctx) {
        Objects.requireNonNull(ctx.exp());
        String name = ctx.ID().getText();

        Exp defValExp = Optional.ofNullable(ctx.initVal())
                .map(ExpParser::parse)
                .orElse(null);
        if (ctx.exp().isEmpty()) { // var
            VarDecl varDecl = VarDecl.of(name, type);
            if (varDecl.isConst()) {
                BaseVal defVal = Optional.ofNullable(defValExp)
                        .map(Exp::constEval)
                        .map(BaseVal.class::cast)
                        .orElse(null);
                SymTab.addConstVar(varDecl, defVal);
                return Collections.emptyList();
            } else {
                SymTab.addVar(varDecl, defValExp);
                return List.of(varDecl);
            }
        } else { // arr
            List<Integer> widths = ctx.exp().stream()
                    .map(ExpParser::parse)
                    .map(Exp::constEval)
                    .map(Val::toInt)
                    .collect(Collectors.toList());
            ArrDecl arrDecl = ArrDecl.of(name, ArrType.of(type, widths));
            if (arrDecl.isConst()) {
                ArrVal defVal = Optional.ofNullable(defValExp)
                        .map(Exp::constEval)
                        .map(ArrVal.class::cast)
                        .orElse(null);
                SymTab.addConstArr(arrDecl, defVal);
            } else {
                SymTab.addArr(arrDecl, (ArrValExp) defValExp);
            }
            return List.of(arrDecl);
        }
    }
}
