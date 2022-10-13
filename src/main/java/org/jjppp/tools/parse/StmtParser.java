package org.jjppp.tools.parse;

import org.jjppp.ast.exp.ValExp;
import org.jjppp.ast.stmt.*;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Val;
import org.jjppp.tools.symtab.SymTab;

import java.util.Optional;

public final class StmtParser extends DefaultVisitor<Stmt> {
    private final static StmtParser INSTANCE = new StmtParser();

    private StmtParser() {
    }

    public static Stmt parse(SysYParser.StmtContext ctx) {
        return ctx.accept(INSTANCE);
    }

    public static Scope parseSCope(SysYParser.ScopeContext ctx) {
        // must be local
        Scope scope = Scope.empty();
        for (SysYParser.BlockItemContext blockItemCtx : ctx.blockItem()) {
            if (blockItemCtx instanceof SysYParser.DeclItemContext declItemCtx) {
                SysYParser.DeclContext declCtx = declItemCtx.decl();
                if (declCtx instanceof SysYParser.ConstDeclContext constDeclCtx) {
                    scope.items().addAll(LocalDeclParser.parse(constDeclCtx));
                } else if (declCtx instanceof SysYParser.VarDeclContext varDeclCtx) {
                    scope.items().addAll(LocalDeclParser.parse(varDeclCtx));
                } else {
                    throw new AssertionError("Should not reach here");
                }
            } else if (blockItemCtx instanceof SysYParser.StmtItemContext stmtItemCtx) {
                scope.add(StmtParser.parse(stmtItemCtx.stmt()));
            } else {
                throw new AssertionError("Should not reach here");
            }
        }
        return scope;
    }

    @Override
    public Stmt visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        return Return.of(
                Optional.ofNullable(ctx.exp())
                        .map(ExpParser::parse)
                        .orElse(ValExp.of(Val.Void.getInstance())));
    }

    @Override
    public Stmt visitAssignStmt(SysYParser.AssignStmtContext ctx) {
        return Assign.of(LValParser.parse(ctx.lVal()), ExpParser.parse(ctx.exp()));
    }

    @Override
    public Stmt visitContinStmt(SysYParser.ContinStmtContext ctx) {
        return Continue.getInstance();
    }

    @Override
    public Stmt visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        return Break.getInstance();
    }

    @Override
    public Stmt visitExpStmt(SysYParser.ExpStmtContext ctx) {
        return ExpStmt.of(ExpParser.parse(ctx.exp()));
    }

    @Override
    public Stmt visitEmptyStmt(SysYParser.EmptyStmtContext ctx) {
        return Empty.getInstance();
    }

    @Override
    public Stmt visitIftStmt(SysYParser.IftStmtContext ctx) {
        return If.of(
                ExpParser.parse(ctx.exp()),
                StmtParser.parse(ctx.stmt()));
    }

    @Override
    public Stmt visitIfteStmt(SysYParser.IfteStmtContext ctx) {
        return Ife.of(
                ExpParser.parse(ctx.exp()),
                StmtParser.parse(ctx.sTru),
                StmtParser.parse(ctx.sFls));
    }

    @Override
    public Stmt visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        return While.of(
                ExpParser.parse(ctx.exp()),
                StmtParser.parse(ctx.stmt()));
    }

    @Override
    public Scope visitBlockStmt(SysYParser.BlockStmtContext ctx) {
        SymTab.getInstance().push();
        Scope scope = parseSCope(ctx.scope());
        SymTab.getInstance().pop();
        return scope;
    }
}
