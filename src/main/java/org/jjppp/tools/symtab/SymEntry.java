package org.jjppp.tools.symtab;

import org.jjppp.type.Type;

public record SymEntry(Symbol symbol, int addr, Type type) {
}
