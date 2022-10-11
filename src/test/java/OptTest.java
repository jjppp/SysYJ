import org.jjppp.ast.Program;
import org.jjppp.ir.IRCode;
import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.cfg.CFGBuilder;
import org.jjppp.tools.optimize.DCE;
import org.jjppp.tools.optimize.LVN;
import org.jjppp.tools.transform.Transform3AC;
import org.junit.jupiter.api.Test;

public final class OptTest {
    @Test
    void testLVN() {
        Program program = ParserTest.fromFile("simple/maxflow.sy");
        IRCode code = Transform3AC.transform(program);
        for (var fun : code.funList()) {
            CFG cfg = CFGBuilder.buildFrom(fun);
            cfg.toFolder("/home/jjppp/tmp/cfg/");
            for (var node : cfg.nodes()) {
                LVN lvn = new LVN();
                Block block = node.block();
                lvn.doLVN(block);
            }
            DCE dce = new DCE();
            dce.doDCE(cfg);
            cfg.toFolder("/home/jjppp/tmp/cfg/opt-");
        }
    }
}
