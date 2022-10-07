package org.jjppp.tools.parse;

import org.jjppp.ast.decl.Decl;
import org.jjppp.parser.SysYParser;
import org.jjppp.type.BaseType;

import java.util.List;
import java.util.stream.Collectors;

public final class LocalDeclParser extends DefaultVisitor<List<Decl>> {
    private final static LocalDeclParser INSTANCE = new LocalDeclParser();

    private LocalDeclParser() {
    }

    public static List<Decl> parse(SysYParser.ConstDeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static List<Decl> parse(SysYParser.VarDeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public List<Decl> visitConstDecl(SysYParser.ConstDeclContext ctx) {
        return ctx.def().stream()
                .map(x -> LocalDefParser.parse(x, TypeParser.parse(ctx.bType(), true)))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitVarDecl(SysYParser.VarDeclContext ctx) {
        BaseType type = TypeParser.parse(ctx.bType(), false);
        return ctx.def().stream()
                .map(x -> LocalDefParser.parse(x, type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
