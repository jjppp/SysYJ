package org.jjppp.ir;

import org.jjppp.ir.instr.Alloc;

import java.util.List;

public record IRCode(List<Fun> funList, List<Alloc> gAllocList) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Alloc alloc : gAllocList()) {
            builder.append(alloc).append("\n");
        }

        for (Fun fun : funList()) {
            builder.append(fun).append("\n");
        }
        return builder.toString();
    }

    public void add(Fun fun) {
        funList.add(fun);
    }

    public void add(Alloc alloc) {
        gAllocList.add(alloc);
    }
}
