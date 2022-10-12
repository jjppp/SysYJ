import org.jjppp.ast.Program;
import org.jjppp.ir.Fun;
import org.jjppp.ir.IRCode;
import org.jjppp.ir.cfg.CFGBuilder;
import org.jjppp.tools.transform.Transform3AC;
import org.junit.jupiter.api.Test;

public final class CFGTest {
    @Test
    void testMaxflow() {
        Program program = ParserTest.fromFile("simple/maxflow.sy");
        IRCode code = Transform3AC.transform(program);
        ParserTest.print("simple/maxflow.sy");

        for (Fun fun : code.funList()) {
            String folderPath = "/tmp/cfg/";
            System.out.println(folderPath);
            CFGBuilder builder = new CFGBuilder(fun);
            builder.build()
                    .toFolder(folderPath);
        }
    }


    @Test
    void testAlloc() {
        Program program = ParserTest.fromFile("simple/alloc.sy");
        IRCode code = Transform3AC.transform(program);
        ParserTest.print("simple/alloc.sy");

        for (Fun fun : code.funList()) {
            String folderPath = "/tmp/cfg/";
            System.out.println(folderPath);
            CFGBuilder builder = new CFGBuilder(fun);
            builder.build().toFolder(folderPath);
        }
    }
}
