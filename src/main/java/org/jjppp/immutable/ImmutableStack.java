package org.jjppp.immutable;

import java.util.Iterator;

public final class ImmutableStack<T> implements Iterable<T> {
    @SuppressWarnings("rawtypes")
    private final static ImmutableStack EMPTY = new ImmutableStack();
    private final ImmutableList<T> list;

    private ImmutableStack() {
        list = ImmutableList.empty();
    }

    private ImmutableStack(ImmutableList<T> list) {
        this.list = list;
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableStack<T> empty() {
        return EMPTY;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImmutableStack<?> rhs) {
            return this.list.equals(rhs.list);
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public ImmutableStack<T> push(T o) {
        return new ImmutableStack<>(list.append(o));
    }

    public T top() {
        if (isEmpty()) {
            throw new RuntimeException("Immutable Stack empty");
        }
        return list.getData();
    }

    public ImmutableStack<T> pop() {
        if (isEmpty()) {
            throw new RuntimeException("Immutable Stack empty");
        }
        return new ImmutableStack<>(list.getRest());
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator(this);
    }

    public final class StackIterator implements Iterator<T> {
        private ImmutableStack<T> stack;

        public StackIterator(ImmutableStack<T> stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            T top = stack.top();
            stack = stack.pop();
            return top;
        }
    }
}
