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

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public final class Interpreter implements InstrVisitor<Integer> {
    private final static boolean TRACE_ON = false;
    private final List<Map<Var, Object>> valMap = new ArrayList<>();
    private final Stack<Map<Label, Integer>> mapStack = new Stack<>();
    private final IRCode code;
    private final PrintStream printStream;
    public int INSTR_COUNT = 0;
    private boolean NEW_LINE = true;
    private byte[] bytes;
    private int curr = 0;

    public Interpreter(IRCode code, File file, FileOutputStream printStream) {
        valMap.add(new HashMap<>());
        this.code = code;
        try (InputStream stream = new FileInputStream(file)) {
            bytes = stream.readAllBytes();
        } catch (IOException e) {
            bytes = null;
        }
        this.printStream = new PrintStream(printStream);
        code.gAllocList().forEach(this::exec);
    }

    private void print(Object o) {
        if (TRACE_ON) {
            System.out.print(o);
        }
    }

    private void println(Object o) {
        print(o);
        print('\n');
    }

    private int fromLabel(Label label) {
        return mapStack.peek().get(label);
    }

    private int exec(Instr instr) {
        return instr.accept(this);
    }

    private int nextCh() {
        if (curr == bytes.length) {
            return -1;
        }
        return bytes[curr++];
    }

    private int nextInt() {
        int ch = nextCh();
        int x = 0, sign = 1;
        while (!(ch >= 48 && ch <= 57)) {
            if (ch == '-') {
                sign = -1;
            }
            ch = nextCh();
        }
        while (ch >= 48 && ch <= 57) {
            x = x * 10 + ch - 48;
            ch = nextCh();
        }
        curr--;
        return x * sign;
    }

    private Object valOf(Ope ope) {
        if (ope instanceof BaseVal baseVal) {
            return Objects.requireNonNull(baseVal);
        } else if (ope instanceof Var var) {
            for (Map<Var, Object> map : valMap) {
                if (map.containsKey(var)) {
                    return map.get(var);
                }
            }
            throw new RuntimeException(ope + ope.getClass().toString());
        }
        throw new RuntimeException("");
    }

    private Object run(Fun.Signature signature, Call call) {
        Fun fun = code.funList().stream()
                .filter(x -> x.signature().equals(signature))
                .findAny().orElseThrow();

        Map<Label, Integer> labelMap = new HashMap<>();
        mapStack.add(labelMap);
        valMap.add(0, new HashMap<>());
        List<Instr> body = fun.body();
        List<Var> args = fun.signature().args();
        for (int i = 0; i < body.size(); ++i) {
            if (body.get(i) instanceof Label label) {
                labelMap.put(label, i + 1);
            }
        }

        IntStream.range(0, args.size())
                .forEach(i -> valMap.get(0).put(args.get(i), valOf(call.args().get(i))));
        Object retVal;
        int pc = 0;

        while (true) {
            INSTR_COUNT += 1;
            Instr instr = body.get(pc);
            if (instr instanceof Ret ret) {
                retVal = ret.retVal();
                if (retVal instanceof Var var) {
                    retVal = valOf(var);
                }
                mapStack.pop();
                valMap.remove(0);
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

    public void run() {
        for (var x : code.funList()) {
            if (x.signature().name().equalsIgnoreCase("_init")) {
                var result = (Int) run(x.signature(), new Call(null, x.signature(), Collections.emptyList()));
                if (!NEW_LINE) printStream.println();
                printStream.println((result.value() % 256 + 256) % 256);
                return;
            }
        }
        throw new RuntimeException("not _init found");
    }

    @Override
    public Integer visit(GAlloc alloc) {
        Var var = alloc.var();
        if (alloc.baseType() instanceof BaseType.Int) {
            valMap.get(0).put(var, new Ptr(alloc.length(), Int.from(0)));
        } else if (alloc.baseType() instanceof BaseType.Float) {
            valMap.get(0).put(var, new Ptr(alloc.length(), Float.from(0)));
        } else {
            throw new RuntimeException("");
        }
        return -1;
    }

    @Override
    public Integer visit(LAlloc alloc) {
        Var var = alloc.var();
        if (alloc.baseType() instanceof BaseType.Int) {
            valMap.get(0).put(var, new Ptr(alloc.length(), Int.from(0)));
        } else if (alloc.baseType() instanceof BaseType.Float) {
            valMap.get(0).put(var, new Ptr(alloc.length(), Float.from(0)));
        } else {
            throw new RuntimeException("");
        }
        return -1;
    }

    @Override
    public Integer visit(BiExp exp) {
        Val lhs = (Val) valOf(exp.lhs());
        Val rhs = (Val) valOf(exp.rhs());
        var res = Objects.requireNonNull(switch (exp.op()) {
            case ADD, PADD, FADD -> lhs.add(rhs);
            case MUL, FMUL -> lhs.mul(rhs);
            case DIV, FDIV -> lhs.div(rhs);
            case SUB, FSUB -> lhs.sub(rhs);
            case EQ -> lhs.eq(rhs);
            case LE -> lhs.le(rhs);
            case GE -> lhs.ge(rhs);
            case LT -> lhs.lt(rhs);
            case GT -> lhs.gt(rhs);
            case NE -> lhs.ne(rhs);
            case OR -> lhs.or(rhs);
            case AND -> lhs.and(rhs);
            case MOD -> lhs.mod(rhs);
        });
        valMap.get(0).put(exp.var(), res);
        println("\t\t" + res);
        return -1;
    }

    @Override
    public Integer visit(UnExp exp) {
        Val val = (Val) valOf(exp.sub());
        var res = switch (exp.op()) {
            case NEG -> val.neg();
            case TOF -> val.toFloat();
            case POS -> val;
            case TOI -> val.toInt();
            case NOT -> val.not();
        };
        valMap.get(0).put(exp.var(), res);
        println("\t\t" + res);
        return -1;
    }

    @Override
    public Integer visit(Call call) {
        println("\t\t" + call.args().stream().map(this::valOf).toList());
        var retVal = run(call.fun(), call);
        valMap.get(0).put(call.var(), retVal);
        return -1;
    }

    @Override
    public Integer visit(LibCall call) {
        String name = call.libFun().name().toLowerCase();
        switch (name) {
            case "putint" -> {
                printStream.print(valOf(call.args().get(0)));
                NEW_LINE = false;
            }
            case "putch" -> {
                var arg1 = valOf(call.args().get(0));
                int intVal = ((Int) arg1).value();
                printStream.print((char) intVal);
                NEW_LINE = intVal == 10;
            }
            case "putarray" -> {
                Int arg1 = (Int) valOf(call.args().get(0));
                Ptr arg2 = (Ptr) valOf(call.args().get(1));
                printStream.print(arg1 + ": ");
                for (int i = 0; i < arg1.value(); ++i) {
                    if (i == 0) {
                        printStream.print(arg2.add(Int.from(i)).read());
                    } else {
                        printStream.print(" " + arg2.add(Int.from(i)).read());
                    }
                }
                NEW_LINE = false;
            }
            case "getint" -> valMap.get(0).put(call.var(), Int.from(nextInt()));
            case "getarray" -> {
                Ptr arg1 = (Ptr) valOf(call.args().get(0));
                int n = nextInt();
                valMap.get(0).put(call.var(), Int.from(n));
                for (int i = 0; i < n; ++i) {
                    var tmp = Int.from(nextInt());
                    arg1.add(Int.from(i)).write(tmp);
                }
            }
            case "getch" -> {
                Int tmp = Int.from(nextCh());
                valMap.get(0).put(call.var(), tmp);
            }
            default -> throw new AssertionError("TODO");
        }
        println("");
        return -1;
    }

    @Override
    public Integer visit(Def def) {
        var val = valOf(def.rhs());
        valMap.get(0).put(def.var(), val);
        println("\t\t" + val);
        return -1;
    }

    @Override
    public Integer visit(Load load) {
        Var var = load.var();
        Ptr ptr = (Ptr) valOf(load.loc());
        var val = ptr.read();
        valMap.get(0).put(var, val);
        println("\t\t" + val);
        return -1;
    }

    @Override
    public Integer visit(Store store) {
        Ptr ptr = (Ptr) valOf(store.var());
        ptr.write((BaseVal) valOf(store.rhs()));
        println("\t\t" + valOf(store.rhs()));
        return -1;
    }

    @Override
    public Integer visit(Label label) {
        return fromLabel(label);
    }

    @Override
    public Integer visit(Br br) {
        if (valMap.get(0).get(br.cond()) instanceof Int i) {
            if (i.value() == 0) {
                println("\t\t" + "FALSE");
                return fromLabel(br.sFls());
            } else {
                println("\t\t" + "TRUE");
                return fromLabel(br.sTru());
            }
        }
        throw new RuntimeException("");
    }

    @Override
    public Integer visit(Jmp jmp) {
        println("\t\t-> " + jmp.target());
        return fromLabel(jmp.target());
    }

    private final static class Ptr implements Val {
        private final Map<Integer, BaseVal> base;
        private final int offset;

        private Ptr(Map<Integer, BaseVal> base, int offset) {
            this.base = base;
            this.offset = offset;
        }

        public Ptr(int length, BaseVal val) {
            base = new HashMap<>(length);
            for (int i = 0; i < length; ++i) {
                base.put(i, val);
            }
            offset = 0;
        }

        @Override
        public String toString() {
            return "base[" + offset + "]";
        }

        public Val read() {
            return base.get(offset);
        }

        public void write(BaseVal val) {
            base.put(offset, val);
        }

        @Override
        public Ptr add(Val delta) {
            if (delta instanceof Int) {
                if (offset + delta.toInt().value() < 0) {
                    throw new RuntimeException("");
                }
                return new Ptr(base, offset + delta.toInt().value());
            }
            throw new RuntimeException("");
        }

        @Override
        public Int toInt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Float toFloat() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(Val val) {
            return 0;
        }
    }
}
