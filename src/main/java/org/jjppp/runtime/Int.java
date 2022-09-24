package org.jjppp.runtime;

public record Int(int value) implements BaseVal {
    @Override
    public int compareTo(Val val) {
        throw new AssertionError("TODO");
    }
}
