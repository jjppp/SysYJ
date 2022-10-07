package org.jjppp.ast;

import org.jjppp.ast.decl.Decl;
import org.jjppp.ast.decl.FunDecl;

import java.util.List;

public record Program(List<FunDecl> funList, List<Decl> globalDecls) {
}
