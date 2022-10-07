package org.jjppp.type;

import org.jjppp.runtime.BaseVal;

public final class VoidType implements BaseType {
    private final static VoidType INSTANCE = new VoidType();

    private VoidType() {
    }

    public static VoidType getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isConst() {
        return true;
    }

    @Override
    public BaseVal defVal() {
        throw new UnsupportedOperationException("voidType defVal()");
    }
}
