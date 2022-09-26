package org.jjppp.runtime;

public record Float(float value) implements BaseVal {
    public static Float from(String rep) {
        return new Float(java.lang.Float.parseFloat(rep));
    }

    @Override
    public int compareTo(Val val) {
        throw new AssertionError("TODO");
    }

    @Override
    public int toInt() {
        return (int) value;
    }
}
