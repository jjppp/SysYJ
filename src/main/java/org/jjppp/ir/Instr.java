package org.jjppp.ir;

import java.util.Set;

public interface Instr {
    void setDead();

    boolean dead();

    Set<Var> useSet();

}
