package org.jjppp.runtime;

import org.jjppp.ir.type.BaseType;

public record Float(java.lang.Float value) implements BaseVal {
    public static Float from(String rep) {
        return new Float(java.lang.Float.parseFloat(rep));
    }

    public static Float from(float value) {
        return new Float(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(Val val) {
        throw new AssertionError("TODO");
    }

    @Override
    public int toInt() {
        return (int) value.floatValue();
    }

    @Override
    public Val add(Val rhs) {
        return Float.from(value + ((Float) rhs).value);
    }

    @Override
    public Val sub(Val rhs) {
        return Float.from(value - ((Float) rhs).value);
    }

    @Override
    public Val mul(Val rhs) {
        return Float.from(value * ((Float) rhs).value);
    }

    @Override
    public Val neg() {
        return Float.from(-value);
    }

    @Override
    public BaseType type() {
        return BaseType.Float.Type();
    }
}
