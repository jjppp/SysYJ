import org.jjppp.Main;
import org.jjppp.ast.Program;
import org.jjppp.ir.IRCode;
import org.jjppp.tools.transform.Transform3AC;
import org.junit.jupiter.api.Test;

public final class OptTest {
    private static void run(String filepath) {
        Program program = ParserTest.fromFile(filepath);
        Transform3AC transformer = new Transform3AC();
        IRCode code = transformer.transform(program);
        Main.optimize(code);
    }

    @Test
    void testDOM() {
        run("official/functional/69_expr_eval.sy");
    }

    @Test
    void testCP() {
        run("simple/cp.sy");
    }

    @Test
    void testDead() {
        run("simple/dead.sy");
    }

    @Test
    void testMaxflow() {
        run("simple/maxflow.sy");
    }

    @Test
    void testMatrix() {
        run("simple/matrix.sy");
    }

    @Test
    void testRedef() {
        run("simple/redef.sy");
    }
}
