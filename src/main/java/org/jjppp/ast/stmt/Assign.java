package org.jjppp.ast.stmt;

import org.jjppp.ast.ASTVisitor;
import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.*;
import org.jjppp.type.ArrType;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public record Assign(LVal lhs, Exp rhs) implements Stmt {
    public static Assign of(LVal lhs, Exp rhs) {
        return new Assign(lhs, rhs);
    }

    public static Assign of(VarDecl var, Exp rhs) {
        return new Assign(VarExp.of(var), rhs);
    }

    private static List<Assign> ofArrRec(ArrDecl arr, ArrType arrType, List<Exp> indices, Queue<Exp> rhs) {
        List<Assign> result = new ArrayList<>();
        for (int i = 0; i < arrType.length(); ++i) {
            indices.add(ValExp.of(i));
            if (rhs.peek() != null) {
                if (arrType.dim() == 1) {
                    Exp exp = rhs.poll();
                    if (!(exp instanceof ValExp valExp && valExp.val() == null)) {
                        ArrAccExp arrAccExp = ArrAccExp.of(arr, indices);
                        result.add(Assign.of(arrAccExp, exp));
                    }
                } else {
                    result.addAll(ofArrRec(arr, (ArrType) arrType.subType(), indices, rhs));
                }
            }
            indices.remove(indices.size() - 1);
        }
        return result;
    }

    public static List<Assign> of(ArrDecl arr) {
        return ofArrRec(arr, arr.type(), new ArrayList<>(), arr.arrValExp().toLinear(arr.type()));
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
