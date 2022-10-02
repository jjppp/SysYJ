package org.jjppp.tools.parse;

import org.jjppp.ast.Item;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.Exp;
import org.jjppp.ast.stmt.Block;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Val;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DeclParser extends DefaultVisitor<List<Item>> {
    private final static DeclParser INSTANCE = new DeclParser();

    private DeclParser() {
    }

    public static List<Item> parse(SysYParser.DeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static List<Item> parse(SysYParser.FuncFParamsContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static List<Item> parse(SysYParser.FuncFParamContext ctx) {
        return ctx.accept(INSTANCE);
    }

    private BaseType parseType(String str, boolean isConst) {
        return switch (str) {
            case "float" -> FloatType.of(isConst);
            case "int" -> IntType.of(isConst);
            case "void" -> VoidType.getInstance();
            default -> throw new ParserException("unknown type");
        };
    }

    private BaseType parseType(SysYParser.BTypeContext ctx, boolean isConst) {
        return parseType(ctx.getText(), isConst);
    }

    private BaseType parseType(SysYParser.FuncTypeContext ctx) {
        return parseType(ctx.getText(), false);
    }

    @Override
    public List<Item> visitConstDecl(SysYParser.ConstDeclContext ctx) {
        return ctx.def().stream()
                .map(x -> DefParser.parse(x, parseType(ctx.bType(), true)))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> visitVarDecl(SysYParser.VarDeclContext ctx) {
        BaseType type = parseType(ctx.bType(), false);
        return ctx.def().stream()
                .map(x -> DefParser.parse(x, type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> visitFuncDecl(SysYParser.FuncDeclContext ctx) {
        String name = ctx.ID().getText();
        List<Decl> params = ctx.funcFParams() == null ?
                Collections.emptyList() :
                DeclParser.parse(ctx.funcFParams()).stream()
                        .map(Decl.class::cast)
                        .collect(Collectors.toList());
        BaseType retType = parseType(ctx.funcType());
        FunDecl funDecl = FunDecl.of(name, FunType.from(retType, params), params, null);
        SymTab.add(funDecl);

        SymTab.push();

        params.forEach(decl -> SymTab.add(decl, null));

        List<Item> blockItems = ctx.block().blockItem().stream()
                .map(ItemParser::parse)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        funDecl.setBody(Block.of(blockItems));

        SymTab.pop();
        return Collections.singletonList(funDecl);
    }

    @Override
    public List<Item> visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        return ctx.funcFParam().stream()
                .map(DeclParser::parse)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> visitVarFParam(SysYParser.VarFParamContext ctx) {
        String name = ctx.ID().getText();
        BaseType bType = parseType(ctx.bType(), false);
        return List.of(VarDecl.of(name, bType));
    }

    @Override
    public List<Item> visitArrFParam(SysYParser.ArrFParamContext ctx) {
        String name = ctx.ID().getText();
        BaseType bType = parseType(ctx.bType(), false);
        List<Integer> widths = ctx.exp().stream()
                .map(ExpParser::parse)
                .map(Exp::constEval)
                .map(Val::toInt).toList();
        return List.of(ArrDecl.of(name, ArrType.of(bType, widths)));
    }
}
