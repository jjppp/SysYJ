package org.jjppp.ir.instr;

import org.jjppp.ir.instr.control.Br;
import org.jjppp.ir.instr.control.Jmp;
import org.jjppp.ir.instr.control.Label;
import org.jjppp.ir.instr.control.Ret;
import org.jjppp.ir.instr.memory.GAlloc;
import org.jjppp.ir.instr.memory.LAlloc;
import org.jjppp.ir.instr.memory.Load;
import org.jjppp.ir.instr.memory.Store;

public interface InstrVisitor<R> {
    default R visitDefault(Instr ignore) {
        throw new AssertionError("TODO");
    }

    default R visit(GAlloc alloc) {
        return visitDefault(alloc);
    }

    default R visit(LAlloc alloc) {
        return visitDefault(alloc);
    }

    default R visit(BiExp exp) {
        return visitDefault(exp);
    }

    default R visit(UnExp exp) {
        return visitDefault(exp);
    }

    default R visit(Call call) {
        return visitDefault(call);
    }

    default R visit(Def def) {
        return visitDefault(def);
    }

    default R visit(Load load) {
        return visitDefault(load);
    }

    default R visit(Store store) {
        return visitDefault(store);
    }

    default R visit(Ret ret) {
        return visitDefault(ret);
    }

    default R visit(Label label) {
        return visitDefault(label);
    }

    default R visit(Br br) {
        return visitDefault(br);
    }

    default R visit(Jmp jmp) {
        return visitDefault(jmp);
    }

    default R visit(LibCall call) {
        return visitDefault(call);
    }
}
