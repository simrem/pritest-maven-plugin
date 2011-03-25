package no.citrus.localprioritization;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnitVisitor extends GenericVisitorAdapter<List<ClassType>, Object>{

	@Override
	public List<ClassType> visit(CompilationUnit cu, Object obj) {
		ArrayList<ClassType> classes = new ArrayList<ClassType>();
		
		if (cu.getTypes() != null) {
			for (TypeDeclaration td : cu.getTypes()) {
				ClassType ct = td.accept(new ClassOrInterfaceDeclarationVisitor(), obj);
				classes.add(ct);
			}
		}
		
		return classes;
	}
}
