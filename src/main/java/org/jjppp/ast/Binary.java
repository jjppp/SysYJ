package org.jjppp.ast;

public abstract class Binary<T> {
    protected final T lhs, rhs;

    protected Binary(T lhs, T rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public T getLhs() {
        return lhs;
    }

    public T getRhs() {
        return rhs;
    }
}
