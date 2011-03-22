package no.citrus.localprioritization;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;


public class MethodDeclarationVisitor<T> extends VoidVisitorAdapter<T> {
	private List<MethodDeclaration> methodDeclarations;
	
	public MethodDeclarationVisitor() {
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	public List<MethodDeclaration> getMethodDeclarations() {
		return methodDeclarations;
	}

	@Override
	public void visit(MethodDeclaration arg0, T arg1) {
		methodDeclarations.add(arg0);
	}
	
	
}
