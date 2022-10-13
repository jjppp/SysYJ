package org.jjppp.tools.interpret;

import org.jjppp.ir.Fun;
import org.jjppp.ir.IRCode;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.ir.instr.*;
import org.jjppp.ir.instr.control.Br;
import org.jjppp.ir.instr.control.Jmp;
import org.jjppp.ir.instr.control.Label;
import org.jjppp.ir.instr.control.Ret;
import org.jjppp.ir.instr.memory.GAlloc;
import org.jjppp.ir.instr.memory.LAlloc;
import org.jjppp.ir.instr.memory.Load;
import org.jjppp.ir.instr.memory.Store;
import org.jjppp.ir.type.BaseType;
import org.jjppp.runtime.BaseVal;
import org.jjppp.runtime.Float;
import org.jjppp.runtime.Int;
import org.jjppp.runtime.Val;

import java.util.*;
import java.util.stream.IntStream;

public final class Interpreter implements InstrVisitor<Integer> {
    private final Map<Var, Object> valMap = new HashMap<>();
    private final Stack<Map<Label, Integer>> mapStack = new Stack<>();
    private final IRCode code;

    public Interpreter(IRCode code) {
        this.code = code;
        code.gAllocList().forEach(this::exec);
    }

    private int fromLabel(Label label) {
        return mapStack.peek().get(label);
    }

    private int exec(Instr instr) {
        return instr.accept(this);
    }

    private Object valOf(Ope ope) {
        if (ope instanceof BaseVal baseVal) {
            return baseVal;
        } else if (ope instanceof Var var) {
            return valMap.get(var);
        }
        throw new RuntimeException("");
    }

    private Object run(Fun fun, Call call) {
        Map<Label, Integer> labelMap = new HashMap<>();
        mapStack.add(labelMap);
        List<Instr> body = fun.body();
        List<Var> args = fun.args();
        for (int i = 0; i < body.size(); ++i) {
            if (body.get(i) instanceof Label label) {
                labelMap.put(label, i + 1);
            }
        }

        IntStream.range(0, args.size())
                .forEach(i -> valMap.put(args.get(i), valOf(call.args().get(i))));
        Object retVal;
        int pc = 0;

        while (true) {
            Instr instr = body.get(pc);
            System.out.println(instr);
            if (instr instanceof Ret ret) {
                retVal = ret.retVal();
                if (retVal instanceof Var var) {
                    retVal = valMap.get(var);
                }
                mapStack.pop();
                args.forEach(valMap::remove);
                return Optional.ofNullable(retVal).orElseThrow();
            } else {
                int res = exec(instr);
                if (res == -1) {
                    pc += 1;
                } else {
                    pc = res;
                }
            }
        }
    }

    public Val run() {
        for (var x : code.funList()) {
            if (x.name().equalsIgnoreCase("_init")) {
                return (Val) run(x, new Call(null, x, Collections.emptyList()));
            }
        }
        throw new RuntimeException("not _init found");
    }

    @Override
    public Integer visit(GAlloc alloc) {
        Var var = alloc.var();
        if (alloc.baseType() instanceof BaseType.Int) {
            valMap.put(var, new Ptr(alloc.length()));
        } else if (alloc.baseType() instanceof BaseType.Float) {
            valMap.put(var, new Ptr(alloc.length()));
        } else {
            throw new RuntimeException("");
        }
        return -1;
    }

    @Override
    public Integer visit(LAlloc alloc) {
        Var var = alloc.var();
        if (alloc.baseType() instanceof BaseType.Int) {
            valMap.put(var, new Ptr(alloc.length()));
        } else if (alloc.baseType() instanceof BaseType.Float) {
            valMap.put(var, new Ptr(alloc.length()));
        } else {
            throw new RuntimeException("");
        }
        return -1;
    }

    @Override
    public Integer visit(BiExp exp) {
        Val lhs = (Val) valOf(exp.lhs());
        Val rhs = (Val) valOf(exp.rhs());
        valMap.put(exp.var(), switch (exp.op()) {
            case FADD, ADD -> lhs.add(rhs);
            case FSUB, SUB -> lhs.sub(rhs);
            case FMUL, MUL -> lhs.mul(rhs);
            case FDIV, DIV -> lhs.div(rhs);
            case MOD -> lhs.mod(rhs);
            case EQ -> Int.from(
                    lhs.equals(rhs) ? 1 : 0);
            case NE -> Int.from(
                    lhs.equals(rhs) ? 0 : 1);
            default -> throw new AssertionError("TODO");
        });
        return -1;
    }

    @Override
    public Integer visit(UnExp exp) {
        Val val = (Val) valOf(exp.sub());
        valMap.put(exp.var(), switch (exp.op()) {
            case NEG -> val.neg();
            case TOF -> Float.from(val.toInt());
            case POS -> val;
            case TOI -> Int.from(val.toInt());
            case NOT -> Int.from((((Int) val).value() != 0) ? 0 : 1);
        });
        return -1;
    }

    @Override
    public Integer visit(Call call) {
        valMap.put(call.var(), run(call.fun(), call));
        return -1;
    }

    @Override
    public Integer visit(LibCall call) {
        return -1;
    }

    @Override
    public Integer visit(Def def) {
        valMap.put(def.var(), valOf(def.rhs()));
        return -1;
    }

    @Override
    public Integer visit(Load load) {
        Var var = load.var();
        Ptr ptr = (Ptr) valOf(load.loc());
        valMap.put(var, ptr.read());
        return -1;
    }

    @Override
    public Integer visit(Store store) {
        Ptr ptr = (Ptr) valOf(store.var());
        ptr.write((Val) valOf(store.rhs()));
        return -1;
    }

    @Override
    public Integer visit(Label label) {
        return fromLabel(label);
    }

    @Override
    public Integer visit(Br br) {
        if (valMap.get(br.cond()) instanceof Int i) {
            if (i.value() == 0) {
                return fromLabel(br.sFls());
            } else {
                return fromLabel(br.sTru());
            }
        }
        throw new RuntimeException("");
    }

    @Override
    public Integer visit(Jmp jmp) {
        return fromLabel(jmp.target());
    }

    private final static class Ptr {
        private final Val[] base;
        private int offset;

        public Ptr(int length) {
            base = new Val[length];
            offset = 0;
        }

        public Val read() {
            return base[offset];
        }

        public void write(Val val) {
            base[offset] = val;
        }

        public void add(Val delta) {
            if (delta instanceof Int) {
                offset += delta.toInt();
            }
            throw new RuntimeException("");
        }
    }
}
