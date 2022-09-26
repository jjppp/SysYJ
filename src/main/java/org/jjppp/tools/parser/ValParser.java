package org.jjppp.tools.parser;

import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Float;
import org.jjppp.runtime.Int;
import org.jjppp.runtime.Val;

public final class ValParser extends DefaultVisitor<Val> {
    private final static ValParser INSTANCE = new ValParser();

    private ValParser() {
    }

    public static Val parse(SysYParser.NumberContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static Val parse(SysYParser.InitValContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public Val visitDecNum(SysYParser.DecNumContext ctx) {
        return Int.fromDec(ctx.getText());
    }

    @Override
    public Val visitHexNum(SysYParser.HexNumContext ctx) {
        return Int.fromHex(ctx.getText());
    }

    @Override
    public Val visitOctNum(SysYParser.OctNumContext ctx) {
        return Int.fromOct(ctx.getText());
    }

    @Override
    public Val visitFltNum(SysYParser.FltNumContext ctx) {
        return Float.from(ctx.getText());
    }
}
