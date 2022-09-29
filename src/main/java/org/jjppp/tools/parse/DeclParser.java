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
import org.jjppp.tools.symtab.SymEntry;
import org.jjppp.tools.symtab.SymTab;
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

    public static List<Decl> parse(SysYParser.FuncFParamsContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static List<Decl> parse(SysYParser.FuncFParamContext ctx) {
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
        List<Decl> params = ctx.funcFParams() == null
                ? Collections.emptyList()
                : DeclParser.parse(ctx.funcFParams());
        BaseType retType = parseType(ctx.funcType());
        FunDecl funDecl = FunDecl.of(name, FunType.from(retType, params), params, null);
        SymTab.add(name, SymEntry.from(funDecl));

        SymTab.push();
        params.forEach(decl -> SymTab.add(decl.name(), SymEntry.from(decl)));

        List<Item> blockItems = ctx.block().blockItem().stream()
                .map(ItemParser::parse)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        funDecl.setBody(Block.of(blockItems));

        SymTab.pop();
        return Collections.singletonList(funDecl);
    }

    @Override
    public List<Decl> visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        return ctx.funcFParam().stream()
                .map(DeclParser::parse)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitVarFParam(SysYParser.VarFParamContext ctx) {
        String name = ctx.ID().getText();
        BaseType bType = parseType(ctx.bType(), false);
        return List.of(VarDecl.of(name, bType, null));
    }

    @Override
    public List<Decl> visitArrFParam(SysYParser.ArrFParamContext ctx) {
        String name = ctx.ID().getText();
        BaseType bType = parseType(ctx.bType(), false);
        List<Integer> widths = ctx.exp().stream()
                .map(ExpParser::parse)
                .map(Exp::constEval)
                .map(Val::toInt).toList();
        return List.of(ArrDecl.of(name, ArrType.of(bType, widths.size(), widths, false), null));
    }
}
