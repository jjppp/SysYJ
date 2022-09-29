package org.jjppp.ast;

public interface ASTNode {
    <R> R accept(ASTVisitor<R> visitor);
}
