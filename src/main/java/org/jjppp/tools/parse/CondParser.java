package org.jjppp.tools.parse;

import org.jjppp.ast.exp.BinExp;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.exp.UnExp;
import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ast.exp.op.UnOp;
import org.jjppp.parser.SysYParser;

public class CondParser extends DefaultVisitor<Exp> {
    private final static CondParser INSTANCE = new CondParser();

    private CondParser() {
    }

    public static Exp parse(SysYParser.CondContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public Exp visitEqCond(SysYParser.EqCondContext ctx) {
        return BinExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '=' -> BiOp.EQ;
                    case '!' -> BiOp.NE;
                    default -> throw new ParserException("unknown op");
                },
                ExpParser.parse(ctx.lhs),
                ExpParser.parse(ctx.rhs));
    }

    @Override
    public Exp visitRawCond(SysYParser.RawCondContext ctx) {
        if (ctx.op == null) {
            return BinExp.of(BiOp.NE, ExpParser.parse(ctx.exp()), 0);
        } else {
            return BinExp.of(BiOp.EQ, ExpParser.parse(ctx.exp()), 0);
        }
    }

    @Override
    public Exp visitUnaryCond(SysYParser.UnaryCondContext ctx) {
        if (ctx.op.getText().charAt(0) == '!') {
            return UnExp.of(UnOp.NOT, CondParser.parse(ctx.cond()));
        }
        throw new ParserException("unknown op");
    }

    @Override
    public Exp visitRelCond(SysYParser.RelCondContext ctx) {
        Exp lhs = ExpParser.parse(ctx.lhs);
        Exp rhs = ExpParser.parse(ctx.rhs);

        if (ctx.op.getText().equals("<")) {
            return BinExp.of(BiOp.LT, lhs, rhs);
        } else if (ctx.op.getText().equals("<=")) {
            return BinExp.of(BiOp.LE, lhs, rhs);
        } else if (ctx.op.getText().equals(">")) {
            return BinExp.of(BiOp.GT, lhs, rhs);
        } else if (ctx.op.getText().equals(">=")) {
            return BinExp.of(BiOp.GE, lhs, rhs);
        }
        throw new ParserException("unknown op");
    }

    @Override
    public Exp visitBinaryCond(SysYParser.BinaryCondContext ctx) {
        return BinExp.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '&' -> BiOp.AND;
                    case '|' -> BiOp.OR;
                    default -> throw new ParserException("unknown op");
                },
                CondParser.parse(ctx.lhs),
                CondParser.parse(ctx.rhs));
    }
}
