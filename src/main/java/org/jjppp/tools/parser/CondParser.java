package org.jjppp.tools.parser;

import org.jjppp.ast.cond.*;
import org.jjppp.ast.exp.Exp;
import org.jjppp.parser.SysYParser;

public class CondParser extends DefaultVisitor<Cond> {
    private final static CondParser INSTANCE = new CondParser();

    private CondParser() {
    }

    public static Cond parse(SysYParser.CondContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public Cond visitEqCond(SysYParser.EqCondContext ctx) {
        return RelCond.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '=' -> Cond.Op.EQ;
                    case '!' -> Cond.Op.NE;
                    default -> throw new ParserException("unknown op");
                },
                ExpParser.parse(ctx.lhs),
                ExpParser.parse(ctx.lhs));
    }

    @Override
    public Cond visitRawCond(SysYParser.RawCondContext ctx) {
        if (ctx.op == null) {
            return RawCond.of(Cond.Op.NZ, ExpParser.parse(ctx.exp()));
        } else {
            return RawCond.of(Cond.Op.IZ, ExpParser.parse(ctx.exp()));
        }
    }

    @Override
    public Cond visitUnaryCond(SysYParser.UnaryCondContext ctx) {
        if (ctx.op.getText().charAt(0) == '!') {
            return UnCond.of(Cond.Op.NOT, CondParser.parse(ctx.cond()));
        }
        throw new ParserException("unknown op");
    }

    @Override
    public Cond visitRelCond(SysYParser.RelCondContext ctx) {
        Exp lhs = ExpParser.parse(ctx.lhs);
        Exp rhs = ExpParser.parse(ctx.rhs);

        if (ctx.op.getText().equals("<")) {
            return RelCond.of(Cond.Op.LT, lhs, rhs);
        } else if (ctx.op.getText().equals("<=")) {
            return RelCond.of(Cond.Op.LE, lhs, rhs);
        } else if (ctx.op.getText().equals(">")) {
            return RelCond.of(Cond.Op.GT, lhs, rhs);
        } else if (ctx.op.getText().equals(">=")) {
            return RelCond.of(Cond.Op.GE, lhs, rhs);
        }
        throw new ParserException("unknown op");
    }

    @Override
    public Cond visitBinaryCond(SysYParser.BinaryCondContext ctx) {
        return BinCond.of(
                switch (ctx.op.getText().charAt(0)) {
                    case '&' -> Cond.Op.AND;
                    case '|' -> Cond.Op.OR;
                    default -> throw new ParserException("unknown op");
                },
                CondParser.parse(ctx.lhs),
                CondParser.parse(ctx.rhs));
    }
}
