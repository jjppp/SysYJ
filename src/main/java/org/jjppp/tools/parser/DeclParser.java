package org.jjppp.tools.parser;

import org.jjppp.ast.Item;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.stmt.Block;
import org.jjppp.parser.SysYParser;
import org.jjppp.type.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DeclParser extends DefaultVisitor<List<Decl>> {
    private final static DeclParser INSTANCE = new DeclParser();

    private DeclParser() {
    }

    public static List<Decl> parse(SysYParser.DeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    private BaseType parseType(SysYParser.BTypeContext ctx, boolean isConst) {
        if (ctx.getText().equals("float")) {
            return FloatType.of(isConst);
        } else if (ctx.getText().equals("int")) {
            return IntType.of(isConst);
        } else {
            throw new ParserException("unknown type");
        }
    }

    private Type parseType(SysYParser.FuncTypeContext ctx) {
        if (ctx.getText().equals("float")) {
            return FloatType.of(false);
        } else if (ctx.getText().equals("int")) {
            return IntType.of(false);
        } else if (ctx.getText().equals("void")) {
            return VoidType.getInstance();
        } else {
            throw new ParserException("unknown type");
        }
    }

    @Override
    public List<Decl> visitConstDecl(SysYParser.ConstDeclContext ctx) {
        return ctx.def().stream()
                .map(x -> DefParser.parse(x, parseType(ctx.bType(), true)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitVarDecl(SysYParser.VarDeclContext ctx) {
        return ctx.def().stream()
                .map(x -> DefParser.parse(x, parseType(ctx.bType(), false)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitFuncDecl(SysYParser.FuncDeclContext ctx) {
        String name = ctx.ID().getText();
        List<Item> items = ctx.block().blockItem().stream()
                .map(ItemParser::parse)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Block body = Block.of(items);
        return Collections.singletonList(FunDecl.of(name, parseType(ctx.funcType()), body));
    }
}
