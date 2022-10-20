package org.jjppp.tools.parse;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.parser.SysYParser;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.BaseType;
import org.jjppp.type.FunType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class GlobalDeclParser extends DefaultVisitor<List<Decl>> {
    private final static GlobalDeclParser INSTANCE = new GlobalDeclParser();

    private GlobalDeclParser() {
    }

    public static List<Decl> parse(SysYParser.DeclContext ctx) {
        return ctx.accept(INSTANCE);
    }

    @Override
    public List<Decl> visitConstDecl(SysYParser.ConstDeclContext ctx) {
        BaseType type = TypeParser.parse(ctx.bType(), true);
        return ctx.ass().stream()
                .map(x -> GlobalDefParser.parse(x, type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitVarDecl(SysYParser.VarDeclContext ctx) {
        BaseType type = TypeParser.parse(ctx.bType(), false);
        return ctx.ass().stream()
                .map(x -> GlobalDefParser.parse(x, type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Decl> visitFuncDecl(SysYParser.FuncDeclContext ctx) {
        String name = ctx.ID().getText();
        List<Decl> params = ctx.funcFParams() == null ?
                Collections.emptyList() :
                ParamParser.parse(ctx.funcFParams()).stream()
                        .map(Decl.class::cast)
                        .collect(Collectors.toList());
        BaseType retType = TypeParser.parse(ctx.funcType());
        FunDecl funDecl = FunDecl.of(name, FunType.from(retType, params), params, null);
        SymTab.getInstance().addFun(funDecl);

        SymTab.getInstance().push();
        {
            params.forEach(decl -> {
                if (decl instanceof ArrDecl arrDecl) {
                    SymTab.getInstance().addArr(arrDecl);
                } else if (decl instanceof VarDecl varDecl) {
                    SymTab.getInstance().addVar(varDecl, null);
                }
            });
            funDecl.setBody(StmtParser.parseSCope(ctx.scope()));
        }
        SymTab.getInstance().pop();
        return Collections.singletonList(funDecl);
    }
}
