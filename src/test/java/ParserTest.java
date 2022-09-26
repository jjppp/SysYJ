import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.runtime.Int;
import org.jjppp.tools.parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ParserTest {
    private final static String path = "/home/jjppp/Code/Project/SysYJ/src/test/resources/simple/";

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
        fromFile("decl.sy");
    }

    @Test
    void testMaxFlow() {
        fromFile("maxflow.sy");
    }
}
