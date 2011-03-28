package no.citrus.localprioritization;

import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCallVisitor extends VoidVisitorAdapter<Object> {

	private List<MethodCall> methodCalls;
	
	public MethodCallVisitor() {
		this.methodCalls = new ArrayList<MethodCall>();
	}

	@Override
	public void visit(MethodCallExpr n, Object arg1) {
		String methodName = n.getName();
		methodCalls.add(new MethodCall(methodName));
	}

	public List<MethodCall> getMethodCalls() {
		return methodCalls;
	}

	
}
