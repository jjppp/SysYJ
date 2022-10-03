package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.*;

import java.util.ArrayList;
import java.util.List;

public record Assign(LVal lhs, Exp rhs) implements Stmt {
    public static Assign of(LVal lhs, Exp rhs) {
        return new Assign(lhs, rhs);
    }

    public static Assign of(VarDecl var, Exp rhs) {
        return new Assign(VarExp.of(var), rhs);
    }

    private static List<Assign> ofArrRec(ArrDecl arr, List<Exp> indices, ArrValExp rhs) {
        List<Assign> result = new ArrayList<>();
        for (int i = 0; i < rhs.exps().size(); ++i) {
            indices.add(ValExp.of(i));
            if (rhs.isLinear()) {
                ArrAccExp arrAccExp = ArrAccExp.of(arr, indices);
                result.add(Assign.of(arrAccExp, rhs.exps().get(i)));
            } else {
                result.addAll(ofArrRec(arr, indices, (ArrValExp) rhs.exps().get(i)));
            }
            indices.remove(indices.size() - 1);
        }
        return result;
    }

    public static List<Assign> of(ArrDecl arr, ArrValExp rhs) {
        return ofArrRec(arr, new ArrayList<>(), rhs);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
