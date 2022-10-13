package org.jjppp.type;

import org.jjppp.runtime.BaseVal;

import java.util.List;

public record ArrType(Type subType, int dim, int length, boolean isConst) implements Type {
    public static ArrType of(Type elemType, int length) {
        if (elemType instanceof ArrType arrType) {
            return new ArrType(arrType, arrType.dim() + 1, length, arrType.isConst());
        }
        return new ArrType(elemType, 1, length, elemType.isConst());
    }

    public static ArrType of(BaseType baseType, List<Integer> widths) {
        ArrType result = ArrType.of(baseType, widths.get(widths.size() - 1));
        for (int i = widths.size() - 2; i >= 0; --i) {
            result = ArrType.of(result, widths.get(i));
        }
        return result;
    }

    @Override
    public int size() {
        return length() * subType().size();
    }

    public int totalLength() {
        if (subType() instanceof ArrType arrType) {
            return length() * arrType.totalLength();
        } else {
            return length();
        }
    }

    @Override
    public boolean isConst() {
        return isConst;
    }

    @Override
    public BaseVal defVal() {
        return subType.defVal();
    }

    public BaseType elemType() {
        if (subType() instanceof ArrType arrType) {
            return arrType.elemType();
        } else if (subType() instanceof BaseType baseType) {
            return baseType;
        }
        throw new AssertionError("TODO");
    }

    @Override
    public String toString() {
        return "arr [" + length() + " x " + subType() + "]";
    }
}
