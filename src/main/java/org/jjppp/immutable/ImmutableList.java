package org.jjppp.immutable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class ImmutableList<T> implements Iterable<T> {
    @SuppressWarnings("rawtypes")
    private final static ImmutableList EMPTY = new ImmutableList();
    private final T data;
    private final ImmutableList<T> rest;
    private final int size;
    // Create empty list
    private ImmutableList() {
        data = null;
        rest = this;
        size = 0;
    }

    private ImmutableList(T data, ImmutableList<T> rest) {
        this.data = data;
        this.rest = rest;
        this.size = rest.size + 1;
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> empty() {
        return (ImmutableList<T>) EMPTY;
    }

    public static <T> ImmutableList<T> of(List<T> list) {
        ImmutableList<T> result = empty();
        for (T x : list) {
            result = result.append(x);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (var x : this) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(x);
        }
        return "[" + builder + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImmutableList<?> list) {
            if (isEmpty() && list.isEmpty()) {
                return true;
            } else if (this.size() != list.size()) {
                return false;
            } else {
                for (ImmutableList<?> x = this, y = list;
                     !x.isEmpty();
                     x = x.getRest(), y = y.getRest()) {
                    if (!x.getData().equals(y.getData())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return this.rest == this;
    }

    public int size() {
        return this.size;
    }

    public T getData() {
        if (isEmpty() || data == null) {
            throw new RuntimeException("Immutable List empty");
        }
        return data;
    }

    public ImmutableList<T> getRest() {
        if (isEmpty() || data == null) {
            throw new RuntimeException("Immutable List empty");
        }
        if (this.size() == 1) {
            return empty();
        }
        return rest;
    }

    public ImmutableList<T> append(T o) {
        Objects.requireNonNull(o);
        return new ImmutableList<>(o, this);
    }

    public List<T> toList() {
        List<T> result = new ArrayList<>();
        for (T data : this) {
            result.add(data);
        }
        return result;
    }

    public ImmutableList<T> reverse() {
        ImmutableList<T> result = empty();
        for (var x = this; !x.isEmpty(); x = x.getRest()) {
            result = result.append(x.getData());
        }
        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new ImmutableListIterator<>(this);
    }

    private final static class ImmutableListIterator<T> implements Iterator<T> {
        private ImmutableList<T> list;

        public ImmutableListIterator(ImmutableList<T> list) {
            this.list = list.reverse();
        }

        @Override
        public boolean hasNext() {
            return !list.isEmpty();
        }

        @Override
        public T next() {
            T data = list.getData();
            list = list.getRest();
            return data;
        }
    }
}
