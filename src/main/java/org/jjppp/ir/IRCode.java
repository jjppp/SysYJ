package org.jjppp.ir;

import java.util.List;

public record IRCode(List<Fun> funList, List<Alloc> allocList) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Alloc alloc : allocList()) {
            builder.append(alloc).append("\n");
        }

        for (Fun fun : funList()) {
            builder.append(fun).append("\n");
        }
        return builder.toString();
    }
}
