package org.jjppp;

import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.ir.IRCode;
import org.jjppp.tools.interpret.Interpreter;
import org.jjppp.tools.parse.Parser;
import org.jjppp.tools.transform.Transform3AC;

import java.io.IOException;

public class Main {
    public static void main(String arg) {
        Program program;
        try {
            Parser parser = new Parser(CharStreams.fromFileName(arg));
            program = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IRCode code = Transform3AC.transform(program);
        System.out.println(code);
        Interpreter interpreter = new Interpreter(code);
        System.out.println(interpreter.run());
    }
}
