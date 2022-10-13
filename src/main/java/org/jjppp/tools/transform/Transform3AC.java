package org.jjppp.tools.transform;

import org.jjppp.ast.ASTNode;
import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.Program;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.UnExp;
import org.jjppp.ast.exp.*;
import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ast.exp.op.UnOp;
import org.jjppp.ast.stmt.*;
import org.jjppp.ir.Fun;
import org.jjppp.ir.IRCode;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.*;
import org.jjppp.ir.instr.control.*;
import org.jjppp.ir.instr.memory.LAlloc;
import org.jjppp.ir.instr.memory.Load;
import org.jjppp.ir.instr.memory.Store;
import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Loc;
import org.jjppp.ir.type.Type;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Int;
import org.jjppp.type.ArrType;
import org.jjppp.type.FunType;

import java.util.*;
import java.util.stream.Collectors;

import static org.jjppp.ast.exp.op.BiOp.PADD;

public final class Transform3AC implements ASTVisitor<Transform3AC.Result> {
    private final static Transform3AC INSTANCE = new Transform3AC();
    private final static Map<String, Fun> globalFun = new HashMap<>();
    private static Var retVar;
    private static Label exit;
    private final Stack<Label> contStack = new Stack<>();
    private final Stack<Label> breakStack = new Stack<>();

    private Transform3AC() {
    }

    public static IRCode transform(Program program) {
        for (FunDecl funDecl : program.funList()) {
            FunType funType = funDecl.type();

            globalFun.put(funDecl.name(), Fun.of(
                    funDecl.name(),
                    BaseType.from(funType.retType()),
                    funType.argTypes().stream()
                            .map(Type::from)
                            .collect(Collectors.toList()),
                    funDecl.getParams().stream()
                            .map(x -> {
                                if (x instanceof VarDecl varDecl) {
                                    return Var.from(varDecl);
                                } else if (x instanceof ArrDecl arrDecl) {
                                    return Var.from(arrDecl);
                                } else {
                                    throw new AssertionError("TODO");
                                }
                            }).collect(Collectors.toList()),
                    new ArrayList<>()));
        }

        IRCode irCode = new IRCode(new ArrayList<>(), new ArrayList<>());

        for (int i = 0; i < program.funList().size(); ++i) {
            FunDecl funDecl = program.funList().get(i);
            Fun fun = globalFun.get(funDecl.name());
            if (!fun.name().equals(funDecl.name())) {
                throw new AssertionError("fun name not match");
            }

            FunType funType = funDecl.type();
            retVar = Var.allocTmp(BaseType.from(funType.retType()));
            exit = LabelFactory.alloc("exit");
            Result body = transform(funDecl.getBody());
            body.add(exit);
            body.add(new Ret(retVar));
            fun.body().addAll(body.block);
            irCode.add(fun);
        }
        for (Decl decl : program.globalDecls()) {
            if (decl instanceof ArrDecl arrDecl) {
                // TODO: check global def using alloc
                irCode.add(Instr.from(Var.from(arrDecl), arrDecl));
            } else if (decl instanceof VarDecl varDecl) {
                irCode.add(Instr.from(Var.from(varDecl), varDecl));
            }
        }
        return irCode;
    }

    public static Result transform(ASTNode node) {
        return node.accept(INSTANCE);
    }

    @Override
    public Result visit(ArrDecl decl) {
        // must be local
        Result result = Result.empty();
        if (decl.isGlobal()) {
            throw new AssertionError("TODO");
        } else {
            Var tmp = new Var(decl, Loc.from(decl), -1);
            result.add(LAlloc.from(tmp, decl));
        }
        return result;
    }

    @Override
    public Result visit(VarDecl decl) {
        // must be local
        Result result;
        if (decl.defValExp().isPresent()) {
            result = transform(decl.defValExp().get());
            result.add(Def.of(Var.from(decl), result.res()));
        } else {
            result = Result.empty();
        }
        return result;
    }

    @Override
    public Result visit(ArrAccExp exp) {
        // must be rhs
        ArrDecl arr = exp.arr();
        Result result;
        if (exp.indices().size() != 0) {
            result = transform(arr.type(), exp.indices());
            Var off = result.res();
            org.jjppp.type.Type expType = exp.type();
            if (expType instanceof org.jjppp.type.BaseType) {
                /* int arr[2][3][4][5]
                 * int c = arr[1][1][1][1]
                 */
                Var tmp = Var.allocTmp(Type.from(expType));
                Var pos = result.alloc(Loc.from(arr), PADD, Var.from(arr), off);
                result.add(new Load(tmp, pos));
            } else {
                /* int arr[2][3][4][5]
                 * int *c = arr[1][1]
                 */
                Var tmp;
                for (int i = exp.indices().size(); i < arr.type().dim(); ++i) {
                    tmp = Var.allocTmp(BaseType.Int.Type());
                    int curWidth = arr.type().widths().get(i);
                    result.add(new BiExp(tmp, BiOp.MUL, off, Int.from(curWidth)));
                    off = tmp;
                }
                result.alloc(Loc.from(arr), PADD, Var.from(arr), off);
            }
        } else {
            result = Result.empty();
            Var rhs = Var.from(arr);
            result.alloc(rhs.type(), rhs);
        }
        return result;
    }

    @Override
    public Result visit(BinExp exp) {
        Result result = transform(exp.getLhs());
        var lhsResVar = result.res();
        result.merge(transform(exp.getRhs()));
        var rhsResVar = result.res();
        result.alloc(Type.from(exp.type()), exp.getOp(), lhsResVar, rhsResVar);
        return result;
    }

    @Override
    public Result visit(FunExp exp) {
        List<Result> argList = exp.args().stream()
                .map(Transform3AC::transform).toList();
        Result result = Result.empty();
        FunDecl fun = exp.fun();

        Var lhs = Var.allocTmp(Type.from(exp.type()));
        Instr call;

        if (fun.getBody() == null) {
            // lib functions
            call = new LibCall(
                    lhs,
                    LibFun.LIB_FUNCTIONS.stream()
                            .filter(x -> fun.name().equalsIgnoreCase(x.name()))
                            .findFirst().orElseThrow(),
                    argList.stream()
                            .map(Result::res)
                            .collect(Collectors.toList()));
        } else {
            call = new Call(
                    lhs,
                    globalFun.get(fun.name()),
                    argList.stream()
                            .map(Result::res)
                            .collect(Collectors.toList()));
        }
        argList.forEach(result::merge);
        result.add(call);
        return result;
    }

    @Override
    public Result visit(UnExp exp) {
        Result result = transform(exp.getSub());
        result.alloc(Type.from(exp.type()), exp.getOp(), result.res());
        return result;
    }

    @Override
    public Result visit(ValExp exp) {
        if (exp.val() instanceof BaseVal baseVal) {
            Result result = Result.empty();
            result.alloc(Type.from(exp.type()), baseVal);
            return result;
        }
        throw new AssertionError("TODO");
    }

    @Override
    public Result visit(VarExp exp) {
        // must be rhs
        Result result = Result.empty();
        VarDecl varDecl = exp.var();

        Var tmp = Var.allocTmp(Type.from(exp.type()));
        if (varDecl.isGlobal()) {
            result.add(new Load(tmp, Var.from(varDecl)));
        } else {
            result.add(Def.of(tmp, Var.from(varDecl)));
        }
        return result;
    }

    private Result transform(ArrType type, List<org.jjppp.ast.exp.Exp> indices) {
        Result result = Result.empty();
        for (int i = 0; i < indices.size(); ++i) {
            var last = result.res();
            var e = indices.get(i);
            int width = type.widths().get(i);
            Result ith = transform(e);
            result.merge(ith);
            if (i != 0) {
                var def1 = result.alloc(BaseType.Int.Type(),
                        BiOp.MUL, last, Int.from(width));
                result.alloc(BaseType.Int.Type(),
                        BiOp.ADD, def1, ith.res());
            }
        }
        return result;
    }

    @Override
    public Result visit(Assign stmt) {
        Result result = transform(stmt.rhs());
        Var rhs = result.res();
        if (stmt.lhs() instanceof VarExp varExp) {
            VarDecl varDecl = varExp.var();
            Var lhs = Var.from(varExp.var());
            if (!varDecl.isGlobal()) {
                if (lhs.type() instanceof Loc) {
                    throw new AssertionError("");
                }
                result.add(Def.of(lhs, rhs));
            } else {
                if (lhs.type() instanceof Loc) {
                    result.add(new Store(lhs, rhs));
                } else {
                    throw new AssertionError("");
                }
            }
            return result;
        } else if (stmt.lhs() instanceof ArrAccExp arrAccExp) {
            /* eval rhs     => rhs
             * eval offset  => off
             * eval ptr     => arr + off
             * store rhs    => *arr = rhs
             */
            result.merge(transform(arrAccExp.arr().type(), arrAccExp.indices()));
            Var off = result.res();
            Var tmp = Var.allocTmp(Loc.from(arrAccExp.arr()));
            result.add(new BiExp(tmp, PADD, Var.from(arrAccExp.arr()), off));
            result.add(new Store(tmp, rhs));
            return result;
        }
        throw new AssertionError("should not reach here");
    }

    @Override
    public Result visit(Scope stmt) {
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
        Label lTru = LabelFactory.alloc("true");
        Label lFls = LabelFactory.alloc("false");
        breakStack.add(lFls);

        Result cond = transform(stmt.cond());
        Result sTru = transform(stmt.sTru());
        Result result = Result.empty();
        result.merge(cond);
        result.add(Br.of(cond.res(), lTru, lFls));
        result.add(lTru);
        result.merge(sTru);
        result.add(lFls);
        breakStack.pop();
        return result;
    }

    @Override
    public Result visit(Ife stmt) {
        Label lTru = LabelFactory.alloc("true");
        Label lFls = LabelFactory.alloc("false");
        Label lEnd = LabelFactory.alloc("end");
        breakStack.add(lEnd);

        Result cond = transform(stmt.cond());
        Result sTru = transform(stmt.sTru());
        Result sFls = transform(stmt.sFls());
        Result result = Result.empty();
        result.merge(cond);
        result.add(Br.of(cond.res(), lTru, lFls));
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
        Result result = Result.empty();
        if (stmt.exp().isPresent()) {
            result = transform(stmt.exp().get());
            result.add(Def.of(retVar, result.res()));
        }
        result.add(new Jmp(exit));
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
        Label lCond = LabelFactory.alloc("cond");
        Label lTru = LabelFactory.alloc("true");
        Label lFls = LabelFactory.alloc("false");
        contStack.add(lCond);
        breakStack.add(lFls);

        Result result = Result.empty();
        Result cond = transform(stmt.cond());
        Result body = transform(stmt.body());

        result.add(lCond);
        result.merge(cond);
        result.add(Br.of(cond.res(), lTru, lFls));
        result.add(lTru);
        result.merge(body);
        result.add(Jmp.of(lCond));
        result.add(lFls);
        contStack.pop();
        breakStack.pop();
        return result;
    }

    public static final class Result {
        private final List<Instr> block;

        private Result(List<Instr> block) {
            this.block = block;
        }

        public static Result of(List<Instr> block) {
            return new Result(block);
        }

        public static Result empty() {
            return new Result(new ArrayList<>());
        }

        public Var alloc(Type type, BiOp op, Ope lhs, Ope rhs) {
            Var tmp = Var.allocTmp(type);
            block.add(new BiExp(tmp, op, lhs, rhs));
            return tmp;
        }

        public Var alloc(Type type, UnOp op, Ope sub) {
            Var tmp = Var.allocTmp(type);
            block.add(new org.jjppp.ir.instr.UnExp(tmp, op, sub));
            return tmp;
        }

        public Var alloc(Type type, Ope rhs) {
            Var tmp = Var.allocTmp(type);
            block.add(new Def(tmp, rhs));
            return tmp;
        }

        public void alloc(Type type, Var loc, Var off) {
            Var var = Var.allocTmp(type);
            Var pos = Var.allocTmp(loc.type());
            block.add(new BiExp(pos, PADD, loc, off));
            Load load = new Load(var, pos);
            block.add(load);
        }

        public Var res() {
            if (block.isEmpty()) {
                return null;
            }
            return block.get(block.size() - 1).var();
        }

        public void add(Instr instr) {
            block.add(instr);
        }

        public void merge(Result rhs) {
            block.addAll(rhs.block);
        }
    }
}
