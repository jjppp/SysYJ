package org.jjppp.tools.parse;

import org.jjppp.parser.SysYParser;
import org.jjppp.type.BaseType;
import org.jjppp.type.FloatType;
import org.jjppp.type.IntType;
import org.jjppp.type.VoidType;

public final class TypeParser {
    public static BaseType parse(String str, boolean isConst) {
        return switch (str) {
            case "float" -> FloatType.of(isConst);
            case "int" -> IntType.of(isConst);
            case "void" -> VoidType.getInstance();
            default -> throw new ParserException("unknown type");
        };
    }

    public static BaseType parse(SysYParser.BTypeContext ctx, boolean isConst) {
        return parse(ctx.getText(), isConst);
    }

    public static BaseType parse(SysYParser.FuncTypeContext ctx) {
        return parse(ctx.getText(), false);
    }
}
