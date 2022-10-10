package org.jjppp.tools.parse;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jjppp.ast.Program;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.stmt.Scope;
import org.jjppp.parser.SysYLexer;
import org.jjppp.parser.SysYParser;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.FunType;
import org.jjppp.type.VoidType;

import java.util.Collections;

public final class Parser {
    private final CharStream charStream;

    public Parser(CharStream cs) {
        charStream = cs;
    }

    public Program parse() {
        SysYLexer lexer = new SysYLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SymTab.init();

        SysYParser parser = new SysYParser(tokens);
        var ps = new ProgramParser();
        Program program = ps.visitCompUnit(parser.compUnit());
        Scope body = SymTab.getInitBlock();
        FunType funType = FunType.from(VoidType.getInstance(), Collections.emptyList());
        FunDecl initFun = FunDecl.of("_init", funType, Collections.emptyList(), body);
        program.funList().add(initFun);
        return program;
    }
}
