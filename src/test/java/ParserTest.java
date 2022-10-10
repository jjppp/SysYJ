import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.runtime.Int;
import org.jjppp.tools.Print;
import org.jjppp.tools.parse.Parser;
import org.jjppp.tools.transform.Transform3AC;
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

    public static Program fromFile(String filename) {
        try {
            Parser parser = new Parser(CharStreams.fromFileName(path + filename));
            return parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(String filename) {
        System.out.println(filename);
        Program program = fromFile(filename);
        Print print = new Print(program);
        System.out.println(print.prettyPrint());
        System.out.println(Transform3AC.transform(program));
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
        print("simple/decl.sy");
    }

    @Test
    void testMaxFlow() {
        print("simple/maxflow.sy");
    }

    @Test
    void testResolve() {
        print("simple/resolve.sy");
    }

    @Test
    void testFib() {
        print("simple/fib.sy");
    }

    @Test
    void testConst() {
        List<String> files = List.of("06_const_var_defn2.sy", "07_const_var_defn3.sy", "08_const_array_defn.sy");
        files.forEach(file -> print("official/functional/" + file));
    }

    @Test
    void testSSA() {
        print("simple/ssa.sy");
    }

    @Test
    void testControl() {
        print("simple/control.sy");
    }
}
