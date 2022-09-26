package org.jjppp.type;

public record IntType(boolean isConst) implements BaseType {
    private final static IntType CONST = new IntType(true);
    private final static IntType NON_CONST = new IntType(false);

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
}
