package org.jjppp.tools.parse;

import org.jjppp.ast.Item;
import org.jjppp.parser.SysYParser;
import org.jjppp.type.BaseType;

import java.util.List;
import java.util.stream.Collectors;

public final class LocalDeclParser extends DefaultVisitor<List<Item>> {
    private final static LocalDeclParser INSTANCE = new LocalDeclParser();

    private LocalDeclParser() {
    }

    public static List<Item> parse(SysYParser.ConstDeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static List<Item> parse(SysYParser.VarDeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public List<Item> visitConstDecl(SysYParser.ConstDeclContext ctx) {
        return ctx.def().stream()
                .map(x -> LocalDefParser.parse(x, TypeParser.parse(ctx.bType(), true)))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> visitVarDecl(SysYParser.VarDeclContext ctx) {
        BaseType type = TypeParser.parse(ctx.bType(), false);
        return ctx.def().stream()
                .map(x -> LocalDefParser.parse(x, type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
