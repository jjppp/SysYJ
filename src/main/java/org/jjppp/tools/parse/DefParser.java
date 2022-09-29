package org.jjppp.tools.parse;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.Exp;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Val;
import org.jjppp.tools.symtab.SymEntry;
import org.jjppp.tools.symtab.SymTab;
import org.jjppp.type.ArrType;
import org.jjppp.type.BaseType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DefParser extends DefaultVisitor<Decl> {
    private final BaseType type;

    private DefParser(BaseType type) {
        this.type = type;
    }

    public static Decl parse(SysYParser.DefContext ctx, BaseType type) {
        return ctx.accept(new DefParser(type));
    }

    @Override
    public Decl visitDef(SysYParser.DefContext ctx) {
        Objects.requireNonNull(ctx.exp());
        String name = ctx.ID().getText();

        Decl result;

        if (ctx.exp().isEmpty()) { // var
            Exp defVal = Optional.ofNullable(ctx.initVal())
                    .map(ExpParser::parse)
                    .orElse(null);
            result = VarDecl.of(name, type, defVal);
        } else { // arr
            Val defVal = Optional.ofNullable(ctx.initVal())
                    .map(ExpParser::parse)
                    .map(Exp::constEval)
                    .orElse(null);
            List<Integer> widths = ctx.exp().stream()
                    .map(ExpParser::parse)
                    .map(Exp::constEval)
                    .map(Val::toInt)
                    .collect(Collectors.toList());
            result = ArrDecl.of(name, ArrType.of(type, widths.size(), widths, type.isConst()), defVal);
        }
        SymTab.add(name, SymEntry.from(result));
        return result;
    }
}
