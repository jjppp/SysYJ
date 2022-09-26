package org.jjppp.tools.parser;

import org.jjppp.ast.Arr;
import org.jjppp.ast.Var;
import org.jjppp.ast.exp.ArrExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.LVal;
import org.jjppp.ast.exp.VarExp;
import org.jjppp.parser.SysYParser;

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
        return VarExp.of(Var.of(ctx.getText()));
    }

    @Override
    public LVal visitArrLVal(SysYParser.ArrLValContext ctx) {
        List<Exp> indices = ctx.exp().stream()
                .map(ExpParser::parse)
                .collect(Collectors.toList());
        return ArrExp.of(Arr.of(ctx.getText()), indices);
    }
}
