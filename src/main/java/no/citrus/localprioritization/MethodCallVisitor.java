package no.citrus.localprioritization;

import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.visitor.GenericVisitorAdapter;
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
        String scope = null;

        if (n.getScope() != null) {
            scope = n.getScope().accept(new ScopeVisitor(), null);
        }

        methodCalls.add(new MethodCall(scope, methodName));
	}

    public List<MethodCall> getMethodCalls() {
		return methodCalls;
	}

    private class ScopeVisitor extends GenericVisitorAdapter<String, Object> {

        @Override
        public String visit(NameExpr n, Object arg) {
            if (n.getName() != null) {
                return n.getName();
            } else {
                return null;
            }
        }
    }
}
