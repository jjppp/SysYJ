package org.jjppp.tools.parse;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jjppp.parser.SysYParser;
import org.jjppp.parser.SysYVisitor;

public class DefaultVisitor<T> implements SysYVisitor<T> {
    private T visitDefault() {
        throw new AssertionError("TODO");
    }

    @Override
    public T visitDef(SysYParser.DefContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitArrInitVal(SysYParser.ArrInitValContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitExpInitVal(SysYParser.ExpInitValContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitCompUnit(SysYParser.CompUnitContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitConstDecl(SysYParser.ConstDeclContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitBType(SysYParser.BTypeContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitVarDecl(SysYParser.VarDeclContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitFuncDecl(SysYParser.FuncDeclContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitFuncType(SysYParser.FuncTypeContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitVarFParam(SysYParser.VarFParamContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitArrFParam(SysYParser.ArrFParamContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitBlock(SysYParser.BlockContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitDeclItem(SysYParser.DeclItemContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitStmtItem(SysYParser.StmtItemContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitAssignStmt(SysYParser.AssignStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitContinStmt(SysYParser.ContinStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitExpStmt(SysYParser.ExpStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitEmptyStmt(SysYParser.EmptyStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitIftStmt(SysYParser.IftStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitIfteStmt(SysYParser.IfteStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitBlockStmt(SysYParser.BlockStmtContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitDecNum(SysYParser.DecNumContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitHexNum(SysYParser.HexNumContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitOctNum(SysYParser.OctNumContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitFltNum(SysYParser.FltNumContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitLValExp(SysYParser.LValExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitBracketExp(SysYParser.BracketExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitValExp(SysYParser.ValExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitAddExp(SysYParser.AddExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitMulExp(SysYParser.MulExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitFunExp(SysYParser.FunExpContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitRawCond(SysYParser.RawCondContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitUnaryCond(SysYParser.UnaryCondContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitRelCond(SysYParser.RelCondContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitBinaryCond(SysYParser.BinaryCondContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitIdLVal(SysYParser.IdLValContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitArrLVal(SysYParser.ArrLValContext ctx) {
        return visitDefault();
    }

    @Override
    public T visitEqCond(SysYParser.EqCondContext ctx) {
        return visitDefault();
    }

    @Override
    public T visit(ParseTree tree) {
        return visitDefault();
    }

    @Override
    public T visitChildren(RuleNode node) {
        return visitDefault();
    }

    @Override
    public T visitTerminal(TerminalNode node) {
        return visitDefault();
    }

    @Override
    public T visitErrorNode(ErrorNode node) {
        return visitDefault();
    }
}
