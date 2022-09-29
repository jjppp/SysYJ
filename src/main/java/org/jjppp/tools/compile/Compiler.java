package org.jjppp.tools.compile;

import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.tools.parse.Parser;

import java.io.IOException;

public final class Compiler {
    private final String filename;

    public Compiler(String filename) {
        this.filename = filename;
    }

    public void compile() {
        try {
            Parser parser = new Parser(CharStreams.fromFileName(filename));
            Program program = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
