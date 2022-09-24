package org.jjppp.runtime;

public record Float(float value) implements BaseVal {
    @Override
    public int compareTo(Val val) {
        throw new AssertionError("TODO");
    }
}
