package org.jjppp.tools.parse;

import org.jjppp.ast.Item;
import org.jjppp.parser.SysYParser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemParser extends DefaultVisitor<List<Item>> {
    private final static ItemParser INSTANCE = new ItemParser();

    private ItemParser() {
    }

    public static List<Item> parse(SysYParser.BlockItemContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public List<Item> visitDeclItem(SysYParser.DeclItemContext ctx) {
        return DeclParser.parse(ctx.decl()).stream()
                .map(Item.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> visitStmtItem(SysYParser.StmtItemContext ctx) {
        return Collections.singletonList(StmtParser.parse(ctx.stmt()));
    }
}
