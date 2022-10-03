package org.jjppp.tools.parse;

import org.jjppp.ast.Program;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.parser.SysYParser;

import java.util.List;
import java.util.stream.Collectors;

public final class ProgramParser extends DefaultVisitor<Program> {
    @Override
    public Program visitCompUnit(SysYParser.CompUnitContext ctx) {
        List<FunDecl> decls = ctx.decl().stream()
                .map(GlobalDeclParser::parse)
                .flatMap(List::stream)
                .filter(FunDecl.class::isInstance)
                .map(FunDecl.class::cast)
                .collect(Collectors.toList());

        return new Program(decls);
    }
}
