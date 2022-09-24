package org.jjppp.ast;

public abstract class Binary<T> {
    protected T lhs, rhs;

    public T getLhs() {
        return lhs;
    }

    public T getRhs() {
        return rhs;
    }
}
