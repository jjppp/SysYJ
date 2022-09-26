package org.jjppp.tools.parser;

import org.jjppp.ast.Fun;
import org.jjppp.ast.exp.*;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.ArrVal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ExpParser extends DefaultVisitor<Exp> {
    private final static ExpParser INSTANCE = new ExpParser();

    private ExpParser() {
    }

    public static Exp parse(SysYParser.ExpContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static Exp parse(SysYParser.InitValContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public Exp visitLValExp(SysYParser.LValExpContext ctx) {
        return LValParser.parse(ctx.lVal());
    }

    @Override
    public Exp visitValExp(SysYParser.ValExpContext ctx) {
        return ValExp.of(ValParser.parse(ctx.number()));
    }

    @Override
    public Exp visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        return UnExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '+' -> OpExp.Op.POS;
                    case '-' -> OpExp.Op.NEG;
                    default -> throw new ParserException("unknown op");
                },
                parse(ctx.exp()));
    }

    @Override
    public Exp visitBracketExp(SysYParser.BracketExpContext ctx) {
        return parse(ctx.exp());
    }

    @Override
    public Exp visitAddExp(SysYParser.AddExpContext ctx) {
        return BinExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '+' -> OpExp.Op.ADD;
                    case '-' -> OpExp.Op.SUB;
                    default -> throw new ParserException("unknown operator");
                },
                parse(ctx.lhs),
                parse(ctx.rhs));
    }

    @Override
    public Exp visitMulExp(SysYParser.MulExpContext ctx) {
        return BinExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '*' -> OpExp.Op.MUL;
                    case '/' -> OpExp.Op.DIV;
                    case '%' -> OpExp.Op.MOD;
                    default -> throw new ParserException("unknown operator");
                },
                parse(ctx.lhs),
                parse(ctx.rhs));
    }

    @Override
    public FunExp visitFunExp(SysYParser.FunExpContext ctx) {
        List<Exp> args = Optional.ofNullable(ctx.funcRParams())
                .map(SysYParser.FuncRParamsContext::exp)
                .orElse(List.of())
                .stream().map(ExpParser::parse)
                .collect(Collectors.toList());
        return FunExp.of(Fun.of(ctx.fun.getText()), args);
    }

    @Override
    public Exp visitExpInitVal(SysYParser.ExpInitValContext ctx) {
        return ExpParser.parse(ctx.exp());
    }

    @Override
    public Exp visitArrInitVal(SysYParser.ArrInitValContext ctx) {
        Objects.requireNonNull(ctx.initVal());

        List<Exp> exps = ctx.initVal().stream()
                .map(ExpParser::parse)
                .toList();
        return ValExp.of(ArrVal.of(exps));
    }
}
