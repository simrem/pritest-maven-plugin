package no.citrus.localprioritization;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;


public class MethodDeclarationVisitor extends VoidVisitorAdapter<Object> {
	private List<MethodDecl> methodDeclarations;
	
	public MethodDeclarationVisitor() {
		methodDeclarations = new ArrayList<MethodDecl>();
	}

	public List<MethodDecl> getMethodDeclarations() {
		return methodDeclarations;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg1) {
		String returnType = null;
		String methodName = n.getName();
		
		if (n.getType() != null) {
			ReturnTypeVisitor rtv = new ReturnTypeVisitor();
			n.getType().accept(rtv, null);
			returnType = rtv.getTypeName();
			
			if (returnType == null) {
				returnType = "void";
			}
		}
		
		methodDeclarations.add(new MethodDecl(returnType, methodName));
	}
	
	private class ReturnTypeVisitor extends VoidVisitorAdapter<Object> {

		private String typeName;

		@Override
		public void visit(ClassOrInterfaceType n, Object arg1) {
			this.typeName = n.getName();
		}
		
		public String getTypeName() {
			return typeName;
		}
	}
}
