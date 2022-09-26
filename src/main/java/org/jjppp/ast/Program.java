package org.jjppp.ast;

import org.jjppp.ast.decl.Decl;

import java.util.List;

public record Program(List<Decl> items) {
}
