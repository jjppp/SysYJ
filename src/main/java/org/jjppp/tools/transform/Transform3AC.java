package org.jjppp.tools.transform;

import org.jjppp.ast.ASTNode;
import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Program;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.*;
import org.jjppp.ast.stmt.*;
import org.jjppp.ir.Exp;
import org.jjppp.ir.*;
import org.jjppp.ir.control.Br;
import org.jjppp.ir.control.Jmp;
import org.jjppp.ir.control.Label;
import org.jjppp.ir.control.Ret;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Int;
import org.jjppp.type.ArrType;
import org.jjppp.type.IntType;
import org.jjppp.type.LocType;
import org.jjppp.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public final class Transform3AC implements ASTVisitor<Transform3AC.Result> {
    private final static Transform3AC INSTANCE = new Transform3AC();
    private final Stack<Label> contStack = new Stack<>();
    private final Stack<Label> breakStack = new Stack<>();

    private Transform3AC() {
    }

    public static IRCode transform(Program program) {
        List<Fun> funList = new ArrayList<>();
        for (FunDecl fun : program.funList()) {
            Fun transformed = Fun.of(
                    fun.name(),
                    fun.type().retType(),
                    fun.type().argTypes().stream()
                            .map(x -> {
                                if (x instanceof ArrType arrType) {
                                    return new LocType(arrType.type());
                                } else {
                                    return x;
                                }
                            }).collect(Collectors.toList()),
                    transform(fun.getBody()).block);
            funList.add(transformed);
        }
        List<Alloc> allocList = new ArrayList<>();
        for (Decl decl : program.globalDecls()) {
            if (decl instanceof ArrDecl arrDecl) {
                allocList.add(Alloc.from(arrDecl));
            } else if (decl instanceof VarDecl varDecl) {
                allocList.add(Alloc.from(varDecl));
            }
        }
        return new IRCode(funList, allocList);
    }

    public static Result transform(ASTNode node) {
        return node.accept(INSTANCE);
    }

    @Override
    public Result visit(ArrDecl decl) {
        Result result = Result.empty();
        result.alloc(LocType.from(decl));
        return result;
    }

    @Override
    public Result visit(VarDecl decl) {
        Result result;
        if (decl.defValExp().isPresent()) {
            result = transform(decl.defValExp().get());
            result.add(Def.of(Var.from(decl), result.res));
        } else {
            result = Result.empty();
            result.add(Def.of(Var.from(decl), decl.type().defVal()));
        }
        return result;
    }

    @Override
    public Result visit(ArrAccExp exp) {
        ArrDecl arr = exp.arr();
        Result result;
        if (exp.indices().size() != 0) {
            result = transform(arr.type(), exp.indices());
            Var off = result.res;
            result.alloc(exp.type(), Var.from(arr), off);
        } else {
            result = Result.empty();
            result.res = Var.from(arr);
        }
        return result;
    }

    @Override
    public Result visit(BinExp exp) {
        Result result = transform(exp.getLhs());
        var lhsResVar = result.res;
        result.merge(transform(exp.getRhs()));
        var rhsResVar = result.res;
        result.alloc(exp.type(), new Exp.BiExp(exp.getOp(), lhsResVar, rhsResVar));
        return result;
    }

    @Override
    public Result visit(FunExp exp) {
        List<Result> argList = exp.args().stream()
                .map(Transform3AC::transform).toList();
        Result result = Result.empty();
        Exp.Call call = new Exp.Call(
                exp.fun(),
                argList.stream()
                        .map(Result::getRes)
                        .collect(Collectors.toList()));
        argList.forEach(result::merge);
        result.alloc(exp.type(), call);
        return result;
    }

    @Override
    public Result visit(UnExp exp) {
        Result result = transform(exp.getSub());
        result.alloc(exp.type(), new Exp.UnExp(exp.getOp(), result.res));
        return result;
    }

    @Override
    public Result visit(ValExp exp) {
        if (exp.val() instanceof BaseVal baseVal) {
            Result result = Result.empty();
            result.alloc(exp.type(), new Exp.UnExp(OpExp.UnOp.NONE, baseVal));
            return result;
        }
        throw new AssertionError("TODO");
    }

    @Override
    public Result visit(VarExp exp) {
        Result result = Result.empty();
        result.alloc(exp.type(), new Exp.UnExp(OpExp.UnOp.NONE, Var.from(exp.var())));
        return result;
    }

    private Result transform(ArrType type, List<org.jjppp.ast.exp.Exp> indices) {
        Result result = Result.empty();
        for (int i = 0; i < indices.size(); ++i) {
            var last = result.res;
            var e = indices.get(i);
            int width = type.widths().get(i);
            Result ith = transform(e);
            result.merge(ith);
            if (i != 0) {
                var def1 = result.alloc(IntType.ofConst(),
                        new Exp.BiExp(OpExp.BiOp.MUL, last, Int.from(width)));
                var def2 = result.alloc(IntType.ofConst(),
                        new Exp.BiExp(OpExp.BiOp.ADD, def1.var(), ith.res));
                result.res = def2.var();
            }
        }
        return result;
    }

    @Override
    public Result visit(Assign stmt) {
        Result result = transform(stmt.rhs());
        Var rhs = result.res;
        if (stmt.lhs() instanceof VarExp varExp) {
            Var lhs = Var.from(varExp.var());
            result.add(Def.of(lhs, rhs));
            result.res = lhs;
            return result;
        } else if (stmt.lhs() instanceof ArrAccExp arrAccExp) {
            result.merge(transform(arrAccExp.arr().type(), arrAccExp.indices()));
            Var off = result.res;
            result.add(new Store(Var.from(arrAccExp.arr()), off, rhs));
            return result;
        }
        throw new AssertionError("should not reach here");
    }

    @Override
    public Result visit(Block stmt) {
        Result result = Result.empty();
        stmt.items().stream()
                .map(Transform3AC::transform)
                .forEach(result::merge);
        return result;
    }

    @Override
    public Result visit(Empty stmt) {
        return Result.empty();
    }

    @Override
    public Result visit(ExpStmt stmt) {
        return transform(stmt.exp());
    }

    @Override
    public Result visit(If stmt) {
        Label lTru = Label.alloc("true");
        Label lFls = Label.alloc("false");
        breakStack.add(lFls);

        Result cond = transform(stmt.cond());
        Result sTru = transform(stmt.sTru());
        Result result = Result.empty();
        result.merge(cond);
        result.add(Br.of(cond.res, lTru, lFls));
        result.add(lTru);
        result.merge(sTru);
        result.add(lFls);
        breakStack.pop();
        return result;
    }

    @Override
    public Result visit(Ife stmt) {
        Label lTru = Label.alloc("true");
        Label lFls = Label.alloc("false");
        Label lEnd = Label.alloc("end");
        breakStack.add(lEnd);

        Result cond = transform(stmt.cond());
        Result sTru = transform(stmt.sTru());
        Result sFls = transform(stmt.sFls());
        Result result = Result.empty();
        result.merge(cond);
        result.add(Br.of(cond.res, lTru, lFls));
        result.add(lTru);
        result.merge(sTru);
        result.add(Jmp.of(lEnd));
        result.add(lFls);
        result.merge(sFls);
        result.add(lEnd);
        breakStack.pop();
        return result;
    }

    @Override
    public Result visit(Return stmt) {
        Result result;
        if (stmt.exp().isPresent()) {
            result = transform(stmt.exp().get());
            result.add(new Ret(Optional.of(result.res)));
        } else {
            result = Result.empty();
            result.add(new Ret(Optional.empty()));
        }
        return result;
    }

    @Override
    public Result visit(Break stmt) {
        Result result = Result.empty();
        result.add(Jmp.of(breakStack.peek()));
        return result;
    }

    @Override
    public Result visit(Continue stmt) {
        Result result = Result.empty();
        result.add(Jmp.of(contStack.peek()));
        return result;
    }

    @Override
    public Result visit(While stmt) {
        Label lCond = Label.alloc("cond");
        Label lTru = Label.alloc("true");
        Label lFls = Label.alloc("false");
        contStack.add(lCond);
        breakStack.add(lFls);

        Result result = Result.empty();
        Result cond = transform(stmt.cond());
        Result body = transform(stmt.body());

        result.add(lCond);
        result.merge(cond);
        result.add(Br.of(cond.res, lTru, lFls));
        result.add(lTru);
        result.merge(body);
        result.add(Jmp.of(lCond));
        result.add(lFls);
        contStack.pop();
        breakStack.pop();
        return result;
    }

    public static final class Result {
        private final List<IR> block;
        private Var res = null;

        private Result(List<IR> block, Var res) {
            this.block = block;
            this.res = res;
        }

        private Result(List<IR> block) {
            this.block = block;
        }

        public static Result of(List<IR> block) {
            return new Result(block);
        }

        public static Result of(Var res) {
            return new Result(new ArrayList<>(), res);
        }

        public static Result empty() {
            return new Result(new ArrayList<>());
        }

        public Def alloc(Type type) {
            return alloc(type, new Exp.UnExp(OpExp.UnOp.NONE, type.defVal()));
        }

        public Def alloc(Type type, Exp rhs) {
            Var var = Var.allocTmp(type);
            Def def = new Def(var, rhs);
            block.add(def);
            res = var;
            return def;
        }

        public void alloc(Type type, Var loc, Var off) {
            Exp.Load load = new Exp.Load(loc, off);
            Var var = Var.allocTmp(type);
            Def def = new Def(var, load);
            block.add(def);
            res = var;
        }

        public void add(IR ir) {
            block.add(ir);
        }

        public void merge(Result rhs) {
            block.addAll(rhs.block);
            res = rhs.res;
        }

        public Var getRes() {
            return res;
        }
    }
}
