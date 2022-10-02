package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Item;
import org.jjppp.ast.decl.Decl;

import java.util.List;
import java.util.stream.Collectors;

public record Block(List<Item> items) implements Stmt {
    public static Block of(List<Item> items) {
        List<Item> sorted = items.stream()
                .filter(Decl.class::isInstance)
                .collect(Collectors.toList());
        sorted.addAll(items.stream().filter(Stmt.class::isInstance).toList());
        return new Block(sorted);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
