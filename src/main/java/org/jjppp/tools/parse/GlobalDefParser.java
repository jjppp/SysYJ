package org.jjppp.tools.parse;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.ArrValExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.ValExp;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Int;
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

        Optional<Exp> defValExp =
                Optional.ofNullable(ctx.initVal())
                        .map(ExpParser::parse);
        VarDecl varDecl = VarDecl.of(name, type, null);
        if (ctx.exp().isEmpty()) { // var
            if (type.isConst()) {
                BaseVal defVal = defValExp
                        .map(Exp::constEval)
                        .map(BaseVal.class::cast)
                        .orElse(null);
                SymTab.getInstance().addConstVar(varDecl, defVal);
                return Collections.emptyList();
            } else {
                SymTab.getInstance().addVar(varDecl, defValExp.orElse(ValExp.of(type.defVal())));
                return List.of(varDecl);
            }
        } else { // arr
            List<Integer> widths = ctx.exp().stream()
                    .map(ExpParser::parse)
                    .map(Exp::constEval)
                    .map(Val::toInt)
                    .map(Int::value)
                    .collect(Collectors.toList());
            ArrType arrType = ArrType.of(type, widths);
            var arrDecl = ArrDecl.of(name, arrType, (ArrValExp) defValExp.orElse(null), true);
            if (type.isConst()) {
                SymTab.getInstance().addArr(arrDecl);
            } else {
                SymTab.getInstance().addArr(arrDecl);
            }
            return List.of(arrDecl);
        }
    }
}
