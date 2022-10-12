package org.jjppp.tools.parse;

import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.exp.*;
import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ast.exp.op.UnOp;
import org.jjppp.parser.SysYParser;
import org.jjppp.tools.symtab.ConstSymEntry;
import org.jjppp.tools.symtab.SymTab;

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
        LVal lVal = LValParser.parse(ctx.lVal());
        String name = lVal.getDecl().name();
        if (SymTab.get(name) instanceof ConstSymEntry) {
            return ValExp.of(lVal.constEval());
        }
        return lVal;
    }

    @Override
    public Exp visitValExp(SysYParser.ValExpContext ctx) {
        return ValExp.of(ValParser.parse(ctx.number()));
    }

    @Override
    public Exp visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        return UnExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '+' -> UnOp.POS;
                    case '-' -> UnOp.NEG;
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
                    case '+' -> BiOp.ADD;
                    case '-' -> BiOp.SUB;
                    default -> throw new ParserException("unknown op");
                },
                parse(ctx.lhs),
                parse(ctx.rhs));
    }

    @Override
    public Exp visitMulExp(SysYParser.MulExpContext ctx) {
        return BinExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '*' -> BiOp.MUL;
                    case '/' -> BiOp.DIV;
                    case '%' -> BiOp.MOD;
                    default -> throw new ParserException("unknown op");
                },
                parse(ctx.lhs),
                parse(ctx.rhs));
    }

    @Override
    public FunExp visitFunExp(SysYParser.FunExpContext ctx) {
        String name = ctx.fun.getText();
        List<Exp> args = Optional.ofNullable(ctx.funcRParams())
                .map(SysYParser.FuncRParamsContext::exp)
                .orElse(List.of())
                .stream().map(ExpParser::parse)
                .collect(Collectors.toList());
        Decl decl = SymTab.get(name).getDecl();
        if (decl instanceof FunDecl funDecl) {
            return FunExp.of(funDecl, args);
        }
        throw new ParserException("undefined symbol " + name);
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
        return ArrValExp.of(exps, exps.stream().noneMatch(ArrValExp.class::isInstance));
    }
}
