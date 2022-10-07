package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Item;

import java.util.ArrayList;
import java.util.List;

public record Block(List<Item> items) implements Stmt {
    public static Block empty() {
        return new Block(new ArrayList<>());
    }

    public static Block of(Item item) {
        return new Block(List.of(item));
    }

    public void add(Item item) {
        items.add(item);
    }

    public void merge(Block after) {
        items.addAll(after.items);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
