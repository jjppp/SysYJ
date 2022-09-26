package org.jjppp.tools.parser;

import org.jjppp.ast.decl.ArrDecl;
import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.VarDecl;
import org.jjppp.ast.exp.Exp;
import org.jjppp.parser.SysYParser;
import org.jjppp.runtime.Val;
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
        if (ctx.exp().isEmpty()) { // var
            Exp defVal = Optional.ofNullable(ctx.initVal())
                    .map(ExpParser::parse)
                    .orElse(null);
            return VarDecl.of(name, type, defVal);
        } else { // arr
            Exp defValExp = Optional.ofNullable(ctx.initVal())
                    .map(ExpParser::parse)
                    .orElse(null);
            List<Integer> widths = ctx.exp().stream()
                    .map(ExpParser::parse)
                    .map(Exp::eval)
                    .map(Val::toInt)
                    .collect(Collectors.toList());
            return ArrDecl.of(name, ArrType.of(type, widths.size(), widths), defValExp);
        }
    }
}
