package org.jjppp.tools;

import org.jjppp.ast.ASTNode;
import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Item;
import org.jjppp.ast.Program;
import org.jjppp.ast.cond.BinCond;
import org.jjppp.ast.cond.RawCond;
import org.jjppp.ast.cond.RelCond;
import org.jjppp.ast.cond.UnCond;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.*;
import org.jjppp.ast.stmt.*;
import org.jjppp.runtime.Val;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public final class Print implements ASTVisitor<String> {
    private static final int TAB_SIZE = 4;
    private final static Map<String, String> opNames = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("LT", "<"),
            new AbstractMap.SimpleImmutableEntry<>("LE", "<="),
            new AbstractMap.SimpleImmutableEntry<>("GE", ">="),
            new AbstractMap.SimpleImmutableEntry<>("GT", ">"),
            new AbstractMap.SimpleImmutableEntry<>("EQ", "=="),
            new AbstractMap.SimpleImmutableEntry<>("NE", "!="),
            new AbstractMap.SimpleImmutableEntry<>("ADD", "+"),
            new AbstractMap.SimpleImmutableEntry<>("SUB", "-"),
            new AbstractMap.SimpleImmutableEntry<>("MUL", "*"),
            new AbstractMap.SimpleImmutableEntry<>("MOD", "%"),
            new AbstractMap.SimpleImmutableEntry<>("DIV", "/"));
    private final Program program;
    private int tabs;

    public Print(Program program) {
        this.program = program;
        tabs = 0;
    }

    public String prettyPrint() {
        return program.items().stream()
                .map(this::print)
                .reduce((x, y) -> x + "\n\n" + y)
                .orElseThrow();
    }

    private String printItems(List<Item> items) {
        return items.stream()
                .map(this::print)
                .reduce((x, y) -> x + newline() + y)
                .orElseThrow();
    }

    private String printExps(List<Exp> exps) {
        return exps.stream()
                .map(this::print)
                .reduce((x, y) -> x + ", " + y)
                .orElseThrow();
    }

    private String print(ASTNode node) {
        return node.accept(this);
    }

    private String newline() {
        return "\n" + " ".repeat(tabs);
    }

    private String bracketOf(ASTNode cont) {
        StringBuilder result = new StringBuilder(" {");
        tabs += TAB_SIZE;
        result.append(newline());
        result.append(print(cont));
        tabs -= TAB_SIZE;
        result.append(newline());
        return result.append("}").toString();
    }

    private String bracketOf(List<Item> items) {
        StringBuilder result = new StringBuilder(" {");
        tabs += TAB_SIZE;
        result.append(newline());
        result.append(printItems(items));
        tabs -= TAB_SIZE;
        result.append(newline());
        return result.append("}").toString();
    }

    @Override
    public String visit(BinCond cond) {
        return "(" + print(cond.getLhs()) + ") "
                + opNames.getOrDefault(cond.getOp().toString(), "unknown")
                + " (" + print(cond.getRhs()) + ")";
    }

    @Override
    public String visit(UnCond cond) {
        return cond.getOp()
                + "(" + print(cond.getSub()) + ")";
    }

    @Override
    public String visit(RawCond cond) {
        return "(" + print(cond.getSub()) + ")";
    }

    @Override
    public String visit(RelCond cond) {
        return "(" + print(cond.getLhs()) + ") "
                + opNames.getOrDefault(cond.getOp().toString(), "unknown")
                + " (" + print(cond.getRhs()) + ")";
    }

    @Override
    public String visit(ArrDecl decl) {
        return decl.type().type()
                + " " + decl.name()
                + decl.type().widths();
    }

    @Override
    public String visit(VarDecl decl) {
        return decl.type()
                + " " + decl.name();
    }

    @Override
    public String visit(FunDecl decl) {
        return decl.type().retType()
                + " " + decl.name()
                + "(" + decl.getParams() + ")"
                + print(decl.getBody());
    }

    @Override
    public String visit(ArrAccExp exp) {
        return exp.arr().name()
                + printExps(exp.indices());
    }

    @Override
    public String visit(ArrValExp exp) {
        return "{" + exp.exps().stream()
                .map(Exp::constEval)
                .map(Val::toString)
                .reduce((x, y) -> x + ", " + y)
                .orElseThrow() + "}";
    }

    @Override
    public String visit(BinExp exp) {
        return "(" + print(exp.getLhs())
                + opNames.getOrDefault(exp.getOp().toString(), "unknown")
                + print(exp.getRhs()) + ")";
    }

    @Override
    public String visit(FunExp exp) {
        return exp.fun().name() + "("
                + exp.args().stream()
                .map(this::print)
                .reduce("", (x, y) -> x + ", " + y)
                + ")";
    }

    @Override
    public String visit(UnExp exp) {
        return exp.getOp() + "("
                + print(exp.getSub()) + ")";
    }

    @Override
    public String visit(ValExp exp) {
        return exp.val().toString();
    }

    @Override
    public String visit(VarExp exp) {
        return exp.var().name();
    }

    @Override
    public String visit(Assign stmt) {
        return print(stmt.lhs()) + " = "
                + print(stmt.rhs());
    }

    @Override
    public String visit(Block stmt) {
        return bracketOf(stmt.items());
    }

    @Override
    public String visit(Break stmt) {
        return "break;";
    }

    @Override
    public String visit(Continue stmt) {
        return "continue;";
    }

    @Override
    public String visit(Empty stmt) {
        return "";
    }

    @Override
    public String visit(ExpStmt stmt) {
        return print(stmt.exp()) + ";";
    }

    @Override
    public String visit(If stmt) {
        return "if (" + print(stmt.cond())
                + ") " + bracketOf(stmt.sTru());
    }

    @Override
    public String visit(Ife stmt) {
        return "if (" + print(stmt.cond())
                + ") " + bracketOf(stmt.sTru())
                + " else " + bracketOf(stmt.sFls());
    }

    @Override
    public String visit(Return stmt) {
        return "return " + stmt.exp().map(this::print).orElse("");
    }

    @Override
    public String visit(While stmt) {
        if (stmt.body() instanceof Block) {
            return "while (" + print(stmt.cond())
                    + ") " + print(stmt.body());
        } else {
            return "while (" + print(stmt.cond())
                    + ") " + bracketOf(stmt.body());
        }
    }
}
