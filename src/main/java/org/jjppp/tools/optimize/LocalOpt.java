package org.jjppp.tools.optimize;

import org.jjppp.ir.cfg.Block;
import org.jjppp.ir.cfg.CFG;
import org.jjppp.tools.optimize.dce.DCE;

public abstract class LocalOpt {
    private final CFG cfg;

    public LocalOpt(CFG cfg) {
        this.cfg = cfg;
    }

    public abstract Block doLocal(Block block);

    public CFG optimize() {
        cfg.nodes().forEach(x -> x.setBlock(doLocal(x.block())));
        DCE.doDCE(cfg);
        return cfg;
    }
}
