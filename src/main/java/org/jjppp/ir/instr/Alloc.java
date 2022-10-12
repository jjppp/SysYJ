package org.jjppp.ir.instr;

import org.jjppp.ir.Var;
import org.jjppp.ir.type.BaseType;

import java.util.Collections;
import java.util.Set;

public abstract class Alloc implements Instr {
    private final BaseType baseType;
    private final Var var;
    private final int length;

    public Alloc(Var var, BaseType baseType, int length) {
        this.var = var;
        this.baseType = baseType;
        this.length = length;
    }

    @Override
    public Set<Var> useSet() {
        return Collections.emptySet();
    }

    @Override
    public Var var() {
        return var;
    }

    public int size() {
        return baseType.size() * length;
    }

    @Override
    public String toString() {
        return var + " = " + "alloc " + length
                + " x [" + baseType + "]";
    }
}
