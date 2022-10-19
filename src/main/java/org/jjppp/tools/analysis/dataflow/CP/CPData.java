package org.jjppp.tools.analysis.dataflow.CP;

import org.jjppp.ir.Var;
import org.jjppp.runtime.BaseVal;
import org.jjppp.tools.analysis.dataflow.AbsData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record CPData(Map<Var, CPFact> varFactMap) implements AbsData<CPData> {
    public CPData(Map<Var, CPFact> varFactMap) {
        this.varFactMap = new HashMap<>(varFactMap);
    }

    public CPFact get(Var var) {
        return varFactMap.getOrDefault(var, CPFact.NAC);
    }

    public void put(Var var, CPFact fact) {
        varFactMap.put(var, fact);
    }

    @Override
    public CPData merge(CPData rhs) {
        Set<Var> keySet = Stream.concat(
                varFactMap.keySet().stream(),
                rhs.varFactMap.keySet().stream()).collect(Collectors.toSet());
        Map<Var, CPFact> result = new HashMap<>();
        for (var key : keySet) {
            var A = varFactMap.getOrDefault(key, CPFact.UNDEF);
            var B = rhs.varFactMap.getOrDefault(key, CPFact.UNDEF);
            result.put(key, A.merge(B));
        }
        return new CPData(result);
    }

    public static final class CPFact {
        public final static CPFact NAC = new CPFact(null);
        public final static CPFact UNDEF = new CPFact(null);
        private final BaseVal val;

        private CPFact(BaseVal val) {
            this.val = val;
        }

        public static CPFact from(BaseVal val) {
            return new CPFact(val);
        }

        public boolean isConst() {
            return this != NAC && this != UNDEF;
        }

        public BaseVal val() {
            return val;
        }

        @Override
        public String toString() {
            if (this == NAC) {
                return "NAC";
            } else if (this == UNDEF) {
                return "UNDEF";
            }
            return "CONST[" + val + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof CPFact cpFact) {
                return Objects.equals(val, cpFact.val);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }

        public CPFact merge(CPFact rhs) {
            if (rhs == NAC || this == NAC) {
                return NAC;
            } else if (this == UNDEF) {
                return rhs;
            } else if (rhs == UNDEF) {
                return this;
            } else {
                return (this.equals(rhs)) ? this : NAC;
            }
        }
    }
}
