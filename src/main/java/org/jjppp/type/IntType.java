package org.jjppp.type;

public final class IntType implements BaseType {
    private final static IntType CONST = new IntType(true);
    private final static IntType NON_CONST = new IntType(false);
    private final boolean isConst;

    private IntType(boolean isConst) {
        this.isConst = isConst;
    }

    public static IntType of(boolean isConst) {
        if (isConst) {
            return ofConst();
        } else {
            return ofNonConst();
        }
    }

    public static IntType ofConst() {
        return CONST;
    }

    public static IntType ofNonConst() {
        return NON_CONST;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public boolean isConst() {
        return isConst;
    }
}
