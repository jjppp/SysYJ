package org.jjppp.type;

public final class FloatType implements BaseType {
    private final static FloatType CONST = new FloatType(true);
    private final static FloatType NON_CONST = new FloatType(false);
    private final boolean isConst;

    private FloatType(boolean isConst) {
        this.isConst = isConst;
    }

    public static FloatType of(boolean isConst) {
        if (isConst) {
            return ofConst();
        } else {
            return ofNonConst();
        }
    }

    public static FloatType ofConst() {
        return CONST;
    }

    public static FloatType ofNonConst() {
        return NON_CONST;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public String toString() {
        return "float";
    }

    @Override
    public boolean isConst() {
        return isConst;
    }
}
