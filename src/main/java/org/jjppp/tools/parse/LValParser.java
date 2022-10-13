package org.jjppp.tools.parse;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.ArrAccExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.LVal;
import org.jjppp.ast.exp.VarExp;
import org.jjppp.parser.SysYParser;
import org.jjppp.tools.symtab.SymTab;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class LValParser extends DefaultVisitor<LVal> {
    private final static LValParser INSTANCE = new LValParser();

    private LValParser() {
    }

    public static LVal parse(SysYParser.LValContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public LVal visitIdLVal(SysYParser.IdLValContext ctx) {
        String name = ctx.ID().getText();
        Decl decl = SymTab.getInstance().get(name).getDecl();
        if (decl instanceof VarDecl varDecl) {
            return VarExp.of(varDecl);
        } else if (decl instanceof ArrDecl arrDecl) {
            return ArrAccExp.of(arrDecl, Collections.emptyList());
        }
        throw new ParserException("undefined symbol " + name);
    }

    @Override
    public LVal visitArrLVal(SysYParser.ArrLValContext ctx) {
        String name = ctx.ID().getText();
        List<Exp> indices = ctx.exp().stream()
                .map(ExpParser::parse)
                .collect(Collectors.toList());
        Decl decl = SymTab.getInstance().get(name).getDecl();
        if (decl instanceof ArrDecl arrDecl) {
            return ArrAccExp.of(arrDecl, indices);
        }
        throw new ParserException("undefined symbol " + name);
    }
}
