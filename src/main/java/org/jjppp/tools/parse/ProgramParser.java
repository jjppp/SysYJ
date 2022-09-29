package org.jjppp.tools.parse;

import org.jjppp.ast.Program;
import org.jjppp.ast.decl.Decl;
import org.jjppp.parser.SysYParser;

import java.util.List;
import java.util.stream.Collectors;

public final class ProgramParser extends DefaultVisitor<Program> {
    @Override
    public Program visitCompUnit(SysYParser.CompUnitContext ctx) {
        List<Decl> decls = ctx.decl().stream()
                .map(DeclParser::parse)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new Program(decls);
    }
}
