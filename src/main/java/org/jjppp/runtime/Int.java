package org.jjppp.runtime;

public record Int(int value) implements BaseVal {
    public static Int from(int value) {
        return new Int(value);
    }

    public static Int fromHex(String rep) {
        if (rep.startsWith("0x")) {
            return new Int(Integer.parseInt(rep.substring(2), 16));
        } else {
            return new Int(Integer.parseInt(rep, 16));
        }
    }

    public static Int fromDec(String rep) {
        return new Int(Integer.parseInt(rep, 10));
    }

    public static Int fromOct(String rep) {
        return new Int(Integer.parseInt(rep, 8));
    }

    @Override
    public int compareTo(Val val) {
        throw new AssertionError("TODO");
    }

    @Override
    public int toInt() {
        return value;
    }
}
