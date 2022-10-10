package org.jjppp.ir;

import java.util.List;

public record IRCode(List<Fun> funList, List<Def> gDefList) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Def def : gDefList()) {
            builder.append(def).append("\n");
        }

        for (Fun fun : funList()) {
            builder.append(fun).append("\n");
        }
        return builder.toString();
    }

    public void add(Fun fun) {
        funList.add(fun);
    }

    public void add(Def def) {
        gDefList.add(def);
    }
}
