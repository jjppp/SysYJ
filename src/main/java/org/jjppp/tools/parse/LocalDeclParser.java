package org.jjppp.tools.parse;

import org.jjppp.ast.stmt.Block;
import org.jjppp.parser.SysYParser;
import org.jjppp.type.BaseType;

public final class LocalDeclParser extends DefaultVisitor<Block> {
    private final static LocalDeclParser INSTANCE = new LocalDeclParser();

    private LocalDeclParser() {
    }

    public static Block parse(SysYParser.ConstDeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static Block parse(SysYParser.VarDeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public Block visitConstDecl(SysYParser.ConstDeclContext ctx) {
        Block block = Block.empty();
        ctx.def().stream()
                .map(x -> LocalDefParser.parse(x, TypeParser.parse(ctx.bType(), true)))
                .forEach(block::merge);
        return block;
    }

    @Override
    public Block visitVarDecl(SysYParser.VarDeclContext ctx) {
        BaseType type = TypeParser.parse(ctx.bType(), false);
        Block block = Block.empty();
        ctx.def().stream()
                .map(x -> LocalDefParser.parse(x, type))
                .forEach(block::merge);
        return block;
    }
}
