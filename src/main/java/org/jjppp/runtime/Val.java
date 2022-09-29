package org.jjppp.runtime;

public interface Val extends Comparable<Val> {
    int toInt();

    default Val add(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val sub(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val mul(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val div(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val mod(Val rhs) {
        throw new UnsupportedOperationException("");
    }

    default Val neg() {
        throw new UnsupportedOperationException("");
    }
}
