package org.jjppp.tools.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jjppp.ast.Program;
import org.jjppp.parser.SysYLexer;
import org.jjppp.parser.SysYParser;

public final class Parser {
    private final CharStream charStream;

    public Parser(CharStream cs) {
        charStream = cs;
    }

    public Program parse() {
        SysYLexer lexer = new SysYLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SysYParser parser = new SysYParser(tokens);
        var ps = new ProgramParser();
        return ps.visitCompUnit(parser.compUnit());
    }
}
