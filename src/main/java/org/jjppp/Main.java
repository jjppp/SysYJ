package org.jjppp;

import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.ir.IRCode;
import org.jjppp.tools.interpret.Interpreter;
import org.jjppp.tools.parse.Parser;
import org.jjppp.tools.transform.Transform3AC;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String source, String input) {
        Program program;
        try {
            Parser parser = new Parser(CharStreams.fromFileName(source));
            program = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        Print print = new Print(program);
//        System.out.println(print.prettyPrint());
        IRCode code = Transform3AC.transform(program);
//        System.out.println(code);
        Interpreter interpreter;
        interpreter = new Interpreter(code, new File(input));
        System.out.println(interpreter.run());
    }
}
