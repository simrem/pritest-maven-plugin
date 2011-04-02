package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.MethodDecl;

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
		
		List<String> parameters = new ArrayList<String>();
		
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				ParameterVisitor pv = new ParameterVisitor();
				p.accept(pv, null);
				parameters.add(pv.getParameterName());
			}
		}
		
		methodDeclarations.add(new MethodDecl(returnType, methodName, parameters));
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
	
	private class ParameterVisitor extends VoidVisitorAdapter<Object> {

		private String parameterName = null;

		@Override
		public void visit(Parameter parameter, Object obj) {
			if (parameter.getType() != null) {
				parameter.getType().accept(this, obj);
			}
		}

		@Override
		public void visit(ClassOrInterfaceType cit, Object obj) {
			parameterName = cit.getName();
		}

		public String getParameterName() {
			return parameterName;
		}
	}
}
