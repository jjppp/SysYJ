package org.jjppp.tools.optimize;

import org.jjppp.ast.exp.OpExp;
import org.jjppp.ir.Exp;
import org.jjppp.ir.Ope;
import org.jjppp.ir.Var;
import org.jjppp.runtime.BaseVal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ValTab {
    private final Map<Val, Integer> valIdMap = new HashMap<>();
    private final Map<Ope, Integer> hold = new HashMap<>();
    private final Map<Integer, ValEntry> tab = new HashMap<>();
    private int ID_COUNT = 0;

    public int from(Ope ope) {
        Val rawVal = new Val.RawVal(ope);
        if (ope instanceof BaseVal) {
            if (!contains(rawVal)) return alloc(rawVal, ope);
            return get(rawVal);
        } else if (ope instanceof Var var) {
            Integer holdingID = holding(var);
            if (holdingID != null) {
                return holdingID;
            } else {
                int id = alloc(rawVal, ope);
                hold.put(ope, id);
                return id;
            }
        }
        throw new AssertionError("TODO");
    }

    public Val from(Exp exp) {
        if (exp instanceof Exp.UnExp unExp) {
            return new Val.UnVal(unExp.op(), from(unExp.sub()));
        } else if (exp instanceof Exp.BiExp biExp) {
            return new Val.BiVal(biExp.op(), from(biExp.lhs()), from(biExp.rhs()));
        } else if (exp instanceof Exp.Load load) {
            return Val.LdVal.alloc(from(load.loc()));
        }
        throw new AssertionError("TODO");
    }

    public boolean contains(Val val) {
        return get(val) != null;
    }

    public int alloc(Val val, Ope belong) {
        valIdMap.put(val, ID_COUNT);
        tab.put(ID_COUNT, new ValEntry(val, ID_COUNT, belong));
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
        record BiVal(OpExp.BiOp op, int lhs, int rhs) implements Val {
        }

        record UnVal(OpExp.UnOp op, int sub) implements Val {
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
        private final Val val;
        private final int id;
        private Ope belong;

        public ValEntry(Val val, int id) {
            this.val = val;
            this.id = id;
            this.belong = null;
        }

        public ValEntry(Val val, int id, Ope belong) {
            this.val = val;
            this.id = id;
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
