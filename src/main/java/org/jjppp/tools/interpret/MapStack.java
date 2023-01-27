package org.jjppp.tools.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MapStack<K, V> {
    private final List<Map<K, V>> stack = new ArrayList<>();

    public V get(K key) {
        for (Map<K, V> varValMap : stack) {
            if (varValMap.containsKey(key)) {
                return varValMap.get(key);
            }
        }
        throw new RuntimeException();
    }

    public void put(K key, V value) {
        stack.get(0).put(key, value);
    }

    public void push() {
        stack.add(0, new HashMap<>());
    }

    public void pop() {
        stack.remove(0);
    }
}
