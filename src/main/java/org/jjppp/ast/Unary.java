package org.jjppp.ast;

public abstract class Unary<T> {
    protected final T sub;

    public Unary(T sub) {
        this.sub = sub;
    }

    public T getSub() {
        return sub;
    }
}
