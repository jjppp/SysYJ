package org.jjppp.ir.instr.memory;

import org.jjppp.ir.Var;
import org.jjppp.ir.instr.Def;
import org.jjppp.ir.type.BaseType;

import java.util.Collections;
import java.util.Set;

public abstract class Alloc extends Def {
    private final BaseType baseType;
    private final int length;

    public Alloc(Var var, BaseType baseType, int length) {
        super(var);
        this.baseType = baseType;
        this.length = length;
    }

    public BaseType baseType() {
        return baseType;
    }

    public int length() {
        return length;
    }

    @Override
    public Set<Var> useSet() {
        return Collections.emptySet();
    }

    public int size() {
        return baseType.size() * length;
    }

    @Override
    public String toString() {
        return var() + " = " + "alloc " + length
                + " x [" + baseType + "]";
    }
}
