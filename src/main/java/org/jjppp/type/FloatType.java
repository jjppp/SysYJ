package org.jjppp.type;

public record FloatType(boolean isConst) implements BaseType {
    private final static FloatType CONST = new FloatType(true);
    private final static FloatType NON_CONST = new FloatType(false);

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
}
