package no.citrus.localprioritization;

import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.visitor.GenericVisitorAdapter;

public class ClassOrInterfaceDeclarationVisitor extends GenericVisitorAdapter<ClassType, Object> {

	@Override
	public ClassType visit(ClassOrInterfaceDeclaration cid, Object obj) {
		String className = cid.getName();
		
		return new ClassType(null, className);
	}
	
}
