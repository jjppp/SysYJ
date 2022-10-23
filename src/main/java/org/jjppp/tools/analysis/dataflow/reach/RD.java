package org.jjppp.tools.analysis.dataflow.reach;

import org.jjppp.ir.Fun;
import org.jjppp.ir.Var;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.ir.instr.Instr;
import org.jjppp.ir.instr.InstrVisitor;
import org.jjppp.tools.analysis.dataflow.DFA;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public final class RD extends DFA<RDData> implements InstrVisitor<RDData> {
    private Map<CFG.Node, RDData> result;

    public RD(CFG cfg) {
        super(cfg);
    }

    public void toFolder(String folder) {
        Fun.Signature fun = cfg.fun();
        try (PrintStream printStream = new PrintStream(folder + fun.name() + ".txt")) {
            for (var entry : result.entrySet()) {
                printStream.println("These definitions reache " + entry.getKey().id());
                for (var x : entry.getValue()) {
                    printStream.println("\t" + x);
                }
                printStream.println();
            }
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    public Map<CFG.Node, RDData> doRD() {
        init();
        result = solve();
        return result;
    }

    @Override
    protected void init() {
        cfg.nodes().forEach(node -> inMap.put(node, new RDData()));
        cfg.nodes().forEach(node -> outMap.put(node, new RDData()));
    }

    @Override
    protected RDData transfer(Instr instr, RDData dataIn) {
        dataIn = new RDData(dataIn);
        Var var = instr.var();
        if (var != null) {
            dataIn.clear(var);
            dataIn.put(var, instr);
        }
        return dataIn;
    }
}
