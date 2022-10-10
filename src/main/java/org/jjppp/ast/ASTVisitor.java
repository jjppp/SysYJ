package org.jjppp.ast;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.*;
import org.jjppp.ast.stmt.*;

public interface ASTVisitor<R> {
    default R visitDefault(ASTNode ignore) {
        throw new AssertionError("TODO");
    }

    default R visit(ArrDecl decl) {
        return visitDefault(decl);
    }

    default R visit(VarDecl decl) {
        return visitDefault(decl);
    }

    default R visit(FunDecl decl) {
        return visitDefault(decl);
    }

    default R visit(ArrAccExp exp) {
        return visitDefault(exp);
    }

    default R visit(ArrValExp exp) {
        return visitDefault(exp);
    }

    default R visit(BinExp exp) {
        return visitDefault(exp);
    }

    default R visit(FunExp exp) {
        return visitDefault(exp);
    }

    default R visit(UnExp exp) {
        return visitDefault(exp);
    }

    default R visit(ValExp exp) {
        return visitDefault(exp);
    }

    default R visit(VarExp exp) {
        return visitDefault(exp);
    }

    default R visit(Assign stmt) {
        return visitDefault(stmt);
    }

    default R visit(Scope stmt) {
        return visitDefault(stmt);
    }

    default R visit(Break stmt) {
        return visitDefault(stmt);
    }

    default R visit(Continue stmt) {
        return visitDefault(stmt);
    }

    default R visit(Empty stmt) {
        return visitDefault(stmt);
    }

    default R visit(ExpStmt stmt) {
        return visitDefault(stmt);
    }

    default R visit(If stmt) {
        return visitDefault(stmt);
    }

    default R visit(Ife stmt) {
        return visitDefault(stmt);
    }

    default R visit(Return stmt) {
        return visitDefault(stmt);
    }

    default R visit(While stmt) {
        return visitDefault(stmt);
    }
}
