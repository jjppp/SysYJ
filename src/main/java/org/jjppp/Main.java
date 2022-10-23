package org.jjppp;

import org.antlr.v4.runtime.CharStreams;
import org.jjppp.ast.Program;
import org.jjppp.ir.Fun;
import org.jjppp.ir.IRCode;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.cfg.CFGBuilder;
import org.jjppp.ir.cfg.CFGLinearize;
import org.jjppp.tools.analysis.dataflow.cp.CP;
import org.jjppp.tools.analysis.dataflow.dom.DOM;
import org.jjppp.tools.analysis.dataflow.reach.RD;
import org.jjppp.tools.analysis.loop.LI;
import org.jjppp.tools.interpret.Interpreter;
import org.jjppp.tools.optimize.dce.DCE;
import org.jjppp.tools.optimize.inv.LoopInv;
import org.jjppp.tools.optimize.lvn.LVN;
import org.jjppp.tools.parse.Parser;
import org.jjppp.tools.transform.Transform3AC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static IRCode optimize(IRCode code) {
        List<Fun> funList = new ArrayList<>();
        for (var fun : code.funList()) {
            CFGBuilder builder = new CFGBuilder(fun);
            CFG cfg = builder.build();
            cfg.toFolder("/home/jjppp/tmp/cfg/");
            cfg.nodes().forEach(x -> x.setBlock(LVN.doLVN(x.block())));
            DCE.doDCE(cfg);
            cfg.toFolder("/home/jjppp/tmp/cfg/O1-");

            CP cp = new CP(cfg);
            cfg = cp.doCP();
            DCE.doDCE(cfg);
            cfg.toFolder("/home/jjppp/tmp/cfg/O2-");

            DOM dom = new DOM(cfg);
            var doms = dom.doDOM();
            LI li = new LI(cfg, doms);
            var loops = li.find();
            RD rd = new RD(cfg);
            var reach = rd.doRD();

            dom.toFolder("/home/jjppp/tmp/cfg/dom-");
            li.toFolder("/home/jjppp/tmp/cfg/loop-");
            rd.toFolder("/home/jjppp/tmp/cfg/reach-");

            for (var loop : loops) {
                for (var node : loop) {
                    LoopInv inv = new LoopInv(node, loop, reach.get(node));
                    inv.mark();
                }
            }
            DCE.doDCE(cfg);
            cfg.toFolder("/home/jjppp/tmp/cfg/O3-");

            funList.add(new CFGLinearize(cfg).toFun());
        }
        return new IRCode(funList, code.gAllocList());
    }

    private static void testOptCode(IRCode code) {
        for (var fun : code.funList()) {
            CFGBuilder builder = new CFGBuilder(fun);
            CFG cfg = builder.build();
            cfg.toFolder("/home/jjppp/tmp/cfg/lin-opt-");
        }
    }

    public static void main(String source, String input, String output) {
        Program program;
        try {
            Parser parser = new Parser(CharStreams.fromFileName(source));
            program = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Transform3AC transformer = new Transform3AC();
        IRCode code = transformer.transform(program);
        IRCode optCode = optimize(code);
        testOptCode(optCode);
        try (FileOutputStream outputStream = new FileOutputStream(output)) {
            Interpreter interpreter = new Interpreter(optCode, new File(input), outputStream);
            interpreter.run();
            System.out.println("INSTR_COUNT: " + interpreter.INSTR_COUNT);
        } catch (IOException e) {
            throw new RuntimeException("cannot open file");
        }
    }
}
