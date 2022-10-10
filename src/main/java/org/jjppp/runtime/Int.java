package org.jjppp.runtime;

import org.jjppp.ir.type.BaseType;

public record Int(Integer value) implements BaseVal {
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
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(Val val) {
        throw new AssertionError("TODO");
    }

    @Override
    public int toInt() {
        return value;
    }

    @Override
    public Val add(Val rhs) {
        return Int.from(this.value + ((Int) rhs).value);
    }

    @Override
    public Val sub(Val rhs) {
        return Int.from(this.value - ((Int) rhs).value);
    }

    @Override
    public Val mul(Val rhs) {
        return Int.from(this.value * ((Int) rhs).value);
    }

    @Override
    public Val div(Val rhs) {
        return Int.from(this.value / ((Int) rhs).value);
    }

    @Override
    public Val mod(Val rhs) {
        return Int.from(this.value % ((Int) rhs).value);
    }

    @Override
    public Val neg() {
        return Int.from(-value);
    }

    @Override
    public BaseType type() {
        return BaseType.Int.Type();
    }
}
