package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Item;

import java.util.List;

public record Block(List<Item> items) implements Stmt {
    public static Block of(List<Item> items) {
        return new Block(items);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
