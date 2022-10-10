import org.jjppp.ast.Program;
import org.jjppp.ir.Fun;
import org.jjppp.ir.IRCode;
import org.jjppp.ir.cfg.CFGBuilder;
import org.jjppp.tools.transform.Transform3AC;
import org.junit.jupiter.api.Test;

public final class CFGTest {
    @Test
    void testCFGtoFile() {
        Program program = ParserTest.fromFile("simple/maxflow.sy");
        IRCode code = Transform3AC.transform(program);
        ParserTest.print("simple/maxflow.sy");

        int cnt = 0;
        for (Fun fun : code.funList()) {
            String filePath = "/tmp/cfg/" + Math.abs(fun.hashCode()) + cnt + ".dot";
            System.out.println(filePath);
            CFGBuilder.buildFrom(fun.body())
                    .toFile(filePath);
            cnt += 1;
        }
    }
}
