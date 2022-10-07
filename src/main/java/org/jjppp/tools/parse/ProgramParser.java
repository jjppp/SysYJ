package org.jjppp.tools.parse;

import org.jjppp.ast.Program;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.parser.SysYParser;

import java.util.ArrayList;
import java.util.List;

public final class ProgramParser extends DefaultVisitor<Program> {
    @Override
    public Program visitCompUnit(SysYParser.CompUnitContext ctx) {
        List<Decl> globalDecls = new ArrayList<>();
        List<FunDecl> funList = new ArrayList<>();
        for (SysYParser.DeclContext declCtx : ctx.decl()) {
            if (declCtx instanceof SysYParser.FuncDeclContext) {
                funList.addAll(GlobalDeclParser.parse(declCtx).stream().map(FunDecl.class::cast).toList());
            } else {
                globalDecls.addAll(GlobalDeclParser.parse(declCtx));
            }
        }
        return new Program(funList, globalDecls);
    }
}
