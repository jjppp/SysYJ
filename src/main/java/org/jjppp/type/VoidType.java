package org.jjppp.type;

public final class VoidType implements BaseType {
    private final static VoidType INSTANCE = new VoidType();

    private VoidType() {
    }

    public static VoidType getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isConst() {
        return true;
    }
}
