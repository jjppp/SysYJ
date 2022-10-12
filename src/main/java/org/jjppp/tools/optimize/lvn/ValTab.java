package org.jjppp.tools.optimize.lvn;

import org.jjppp.ast.exp.op.BiOp;
import org.jjppp.ast.exp.op.UnOp;
import org.jjppp.ir.Ope;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ValTab {
    private final Map<Val, Integer> valIdMap = new HashMap<>();
    private final Map<Ope, Integer> hold = new HashMap<>();
    private final Map<Integer, ValEntry> tab = new HashMap<>();
    private int ID_COUNT = 0;

    public boolean contains(Val val) {
        return get(val) != null;
    }

    public int alloc(Val val, Ope belong) {
        valIdMap.put(val, ID_COUNT);
        tab.put(ID_COUNT, new ValEntry(belong));
        return ID_COUNT++;
    }

    public ValEntry getEntry(int id) {
        return tab.get(id);
    }

    public Integer get(Val val) {
        return valIdMap.get(val);
    }

    public void hold(Ope ope, int id) {
        hold.put(ope, id);
    }

    public void unhold(Ope ope) {
        hold.remove(ope);
    }

    public Integer holding(Ope ope) {
        return hold.get(ope);
    }

    public Ope belong(int id) {
        return getEntry(id).belong();
    }

    public void setBelong(int id, Ope belong) {
        tab.get(id).setBelong(belong);
    }

    interface Val {
        record BiVal(BiOp op, int lhs, int rhs) implements Val {
        }

        record UnVal(UnOp op, int sub) implements Val {
        }

        final class LdVal implements Val {
            private static int LD_COUNT = 0;
            private final int loc;
            private final int id;

            private LdVal(int loc, int id) {
                this.loc = loc;
                this.id = id;
            }

            public static LdVal alloc(int loc) {
                return new LdVal(loc, LD_COUNT++);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o instanceof LdVal ldVal) {
                    return id == ldVal.id;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hash(id, loc);
            }

            public int loc() {
                return loc;
            }
        }

        record RawVal(Ope ope) implements Val {
        }
    }

    public static final class ValEntry {
        private Ope belong;

        public ValEntry(Ope belong) {
            this.belong = belong;
        }

        public Ope belong() {
            return belong;
        }

        public void setBelong(Ope belong) {
            this.belong = belong;
        }
    }
}
