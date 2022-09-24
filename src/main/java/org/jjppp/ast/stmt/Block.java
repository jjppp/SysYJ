package org.jjppp.ast.stmt;

import org.jjppp.ast.Item;

import java.util.List;

public record Block(List<Item> items) implements Stmt {
}
