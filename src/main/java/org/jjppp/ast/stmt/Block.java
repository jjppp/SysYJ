package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Item;
import org.jjppp.ast.decl.Decl;

import java.util.ArrayList;
import java.util.List;

public record Block(List<Decl> decls, List<Stmt> stmts) implements Stmt {
    public static Block empty() {
        return new Block(new ArrayList<>(), new ArrayList<>());
    }

    public void add(Item item) {
        if (item instanceof Decl decl) {
            decls.add(decl);
        } else if (item instanceof Stmt stmt) {
            stmts.add(stmt);
        }
    }

    public void merge(Block after) {
        decls.addAll(after.decls);
        stmts.addAll(after.stmts);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
