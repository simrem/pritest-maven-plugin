package no.citrus.localprioritization;

import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallVisitor<T> extends VoidVisitorAdapter<T> {

	private List<MethodCallExpr> methodCalls;
	
	public MethodCallVisitor() {
		this.methodCalls = new ArrayList<MethodCallExpr>();
	}

	@Override
	public void visit(MethodCallExpr arg0, T arg1) {
		methodCalls.add(arg0);
	}

	public List<MethodCallExpr> getMethodCalls() {
		return methodCalls;
	}

	
}
