package org.jjppp.ast.stmt;

import org.jjppp.ast.Item;

import java.util.List;

public record Block(List<Item> items) implements Stmt {
    public static Block of(List<Item> items) {
        return new Block(items);
    }
}
