package org.jjppp.ast;

import org.jjppp.ast.cond.BinCond;
import org.jjppp.ast.cond.RawCond;
import org.jjppp.ast.cond.RelCond;
import org.jjppp.ast.cond.UnCond;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.*;
import org.jjppp.ast.stmt.*;

public interface ASTVisitor<R> {
    default R visitDefault(ASTNode node) {
        throw new AssertionError("TODO");
    }

    R visit(BinCond cond);

    R visit(UnCond cond);

    R visit(RawCond cond);

    R visit(RelCond cond);

    R visit(ArrDecl decl);

    R visit(VarDecl decl);

    R visit(FunDecl decl);

    R visit(ArrAccExp exp);

    R visit(ArrValExp exp);

    R visit(BinExp exp);

    R visit(FunExp exp);

    R visit(UnExp exp);

    R visit(ValExp exp);

    R visit(VarExp exp);

    R visit(Assign stmt);

    R visit(Block stmt);

    R visit(Break stmt);

    R visit(Continue stmt);

    R visit(Empty stmt);

    R visit(ExpStmt stmt);

    R visit(If stmt);

    R visit(Ife stmt);

    R visit(Return stmt);

    R visit(While stmt);
}
