package org.jjppp.tools.parse;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.Exp;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Val;
import org.jjppp.type.ArrType;
import org.jjppp.type.BaseType;

import java.util.List;
import java.util.stream.Collectors;

public final class ParamParser extends DefaultVisitor<List<Decl>> {
    private final static ParamParser INSTANCE = new ParamParser();

    private ParamParser() {
    }

    public static List<Decl> parse(SysYParser.FuncFParamsContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static List<Decl> parse(SysYParser.FuncFParamContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public List<Decl> visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        return ctx.funcFParam().stream()
                .map(ParamParser::parse)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitVarFParam(SysYParser.VarFParamContext ctx) {
        String name = ctx.ID().getText();
        BaseType bType = TypeParser.parse(ctx.bType(), false);
        return List.of(VarDecl.of(name, bType, null, false));
    }

    @Override
    public List<Decl> visitArrFParam(SysYParser.ArrFParamContext ctx) {
        String name = ctx.ID().getText();
        BaseType bType = TypeParser.parse(ctx.bType(), false);
        List<Integer> widths = ctx.exp().stream()
                .map(ExpParser::parse)
                .map(Exp::constEval)
                .map(Val::toInt).collect(Collectors.toList());
        widths.add(0, Integer.MAX_VALUE);
        return List.of(ArrDecl.of(name, ArrType.of(bType, widths), false));
    }
}
