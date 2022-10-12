package org.jjppp.ir.instr;

import org.jjppp.ir.control.Br;
import org.jjppp.ir.control.Jmp;
import org.jjppp.ir.control.Label;
import org.jjppp.ir.control.Ret;

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
}
