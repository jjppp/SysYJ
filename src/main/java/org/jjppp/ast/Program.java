package org.jjppp.ast;

import org.jjppp.ast.decl.FunDecl;

import java.util.List;

public record Program(List<FunDecl> items) {
}
