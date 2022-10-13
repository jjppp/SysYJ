package org.jjppp.ir.instr;

import org.jjppp.ir.type.BaseType;
import org.jjppp.ir.type.Loc;
import org.jjppp.ir.type.Type;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record LibFun(String name, BaseType retType, List<Type> argTypes) {
    public static final Set<LibFun> LIB_FUNCTIONS = Stream.of(
            new LibFun("GETINT", BaseType.Int.Type(), Collections.emptyList()),
            new LibFun("GETCH", BaseType.Int.Type(), Collections.emptyList()),
            new LibFun("GETFLOAT", BaseType.Float.Type(), Collections.emptyList()),
            new LibFun("GETARRAY", BaseType.Int.Type(), List.of(Loc.of(BaseType.Int.Type()))),
            new LibFun("GETFARRAY", BaseType.Int.Type(), List.of(Loc.of(BaseType.Float.Type()))),
            new LibFun("PUTINT", BaseType.Void.Type(), List.of(BaseType.Int.Type())),
            new LibFun("PUTCH", BaseType.Void.Type(), List.of(BaseType.Int.Type())),
            new LibFun("PUTFLOAT", BaseType.Void.Type(), List.of(BaseType.Float.Type())),
            new LibFun("PUTARRAY", BaseType.Void.Type(), List.of(BaseType.Int.Type(), Loc.of(BaseType.Float.Type()))),
            new LibFun("PUTFARRAY", BaseType.Void.Type(), List.of(BaseType.Int.Type(), Loc.of(BaseType.Float.Type())))
//            new LibFun("PUTF",      BaseType.Void.Type(), Collections.emptyList()),
    ).collect(Collectors.toSet());
}
