package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Item;

import java.util.ArrayList;
import java.util.List;

public record Scope(List<Item> items) implements Stmt {
    public static Scope empty() {
        return new Scope(new ArrayList<>());
    }

    public static Scope of(Item item) {
        return new Scope(List.of(item));
    }

    public void add(Item item) {
        items.add(item);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
