package org.jjppp.ir.mem;

import org.jjppp.ir.Exp;
import org.jjppp.ir.Var;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Loc;
import org.jjppp.ir.type.Type;

import java.util.Collections;
import java.util.Set;

public abstract class Alloc implements Exp {
    private final BaseType baseType;

    private final int length;

    public Alloc(BaseType baseType, int length) {
        this.baseType = baseType;
        this.length = length;
    }

    public int size() {
        return baseType.size() * length;
    }

    @Override
    public Set<Var> useSet() {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "alloc " + length
                + " x [" + baseType + "]";
    }

    @Override
    public Type type() {
        return Loc.of(baseType);
    }
}
