package no.citrus.localprioritization.visitor;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.RawMethodCall;
import no.citrus.localprioritization.model.ReferenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodCallVisitor extends VoidVisitorAdapter<Object> {

	private List<RawMethodCall> methodCalls;
	private final Map<String, ReferenceType> localVariables;
	private final Map<String, ReferenceType> fieldVariables;
	
	public MethodCallVisitor(Map<String, ReferenceType> localVariables, Map<String, ReferenceType> fieldVariables) {
		this.localVariables = localVariables;
		this.fieldVariables = fieldVariables;
		this.methodCalls = new ArrayList<RawMethodCall>();
	}

    @Override
	public void visit(MethodCallExpr n, Object arg1) {
		String methodName = n.getName();
        String scope = null;
        List<String> parameters = new ArrayList<String>();

        if (n.getScope() != null) {
            scope = n.getScope().accept(new ScopeVisitor(), null);
        }
        
        if (n.getArgs() != null) {
        	for (Expression expr : n.getArgs()) {
        		String argument = expr.accept(new ArgumentVisitor(), null);
        		System.out.println("--- expr: " + expr.toString() + " --- argument: " + argument);
        	}
        }
        
        RawMethodCall methodCall = new RawMethodCall(scope, methodName, parameters);
        methodCalls.add(methodCall);
	}

	public List<RawMethodCall> getRawMethodCalls() {
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
    
    private class ScopedMethodCallVisitor extends GenericVisitorAdapter<NestedMethodCall, Object> {
    	
    	@Override
		public NestedMethodCall visit(MethodCallExpr n, Object arg1) {
			//System.out.println("  ScopedMethodCallVisitor.MethodCallExpr: " + n.getScope().toString() + " " + n.getName());
			
			String methodName = n.getName();
			String scope = null;
			NestedMethodCall nestedCall = null;
			
			if (n.getScope() != null) {
				scope = n.getScope().accept(new ScopeVisitor(), null);
				nestedCall = n.getScope().accept(new ScopedMethodCallVisitor(), null);
			}
			
			return new NestedMethodCall(scope, methodName, nestedCall);
		}
    }
    
    private class NestedMethodCall {

		private final String scope;
		private final String methodName;
		private final NestedMethodCall nestedCall;

		public NestedMethodCall(String scope, String methodName, NestedMethodCall nestedCall) {
			this.scope = scope;
			this.methodName = methodName;
			this.nestedCall = nestedCall;
		}

		public String getScope() {
			return scope;
		}

		public String getMethodName() {
			return methodName;
		}

		public NestedMethodCall getNestedCall() {
			return nestedCall;
		}
    }
    
    public class ArgumentVisitor extends GenericVisitorAdapter<String, Object> {

		@Override
		public String visit(NullLiteralExpr n, Object arg) {
			System.out.println("---- NullLiteralExpr");
			return "null";
		}

		@Override
		public String visit(ObjectCreationExpr arg0, Object arg1) {
			System.out.println("---- ObjectCreationExpr");
			return arg0.getType().getName();
		}

    	
	}
}
