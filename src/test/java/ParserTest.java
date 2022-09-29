import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.runtime.Int;
import org.jjppp.tools.Print;
import org.jjppp.tools.parse.Parser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ParserTest {
    private final static String path = "/home/jjppp/Code/Project/SysYJ/src/test/resources/";

    private static Program fromCode(String code) {
        Parser parser = new Parser(CharStreams.fromString(code));
        return parser.parse();
    }

    private static Program fromFile(String filename) {
        try {
            Parser parser = new Parser(CharStreams.fromFileName(path + filename));
            return parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testVal() {
        assertEquals(Int.from(11), Int.fromDec("11"));
        assertEquals(Int.from(17), Int.fromHex("11"));
        assertEquals(Int.from(17), Int.fromHex("0x11"));
        assertEquals(Int.from(17), Int.fromHex("11"));
        assertEquals(Int.from(1), Int.fromOct("01"));
        assertEquals(Int.from(9), Int.fromOct("011"));
    }

    @Test
    void testDecl() {
        fromFile("simple/decl.sy");
    }

    @Test
    void testMaxFlow() {
        fromFile("simple/maxflow.sy");
    }

    @Test
    void testResolve() {
        fromFile("simple/resolve.sy");
    }

    @Test
    void testFib() {
        Program program = fromFile("simple/fib.sy");
        Print print = new Print(program);
        System.out.println(print.prettyPrint());
    }

    @Test
    void testConst() {
        List<String> files = List.of("06_const_var_defn2.sy", "07_const_var_defn3.sy", "08_const_array_defn.sy");
        for (String file : files) {
            System.out.println(file);
            Print print = new Print(fromFile("official/functional/" + file));
            System.out.println(print.prettyPrint());
        }
    }
}
