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
            NestedMethodCall nestedCall = n.getScope().accept(new ScopedMethodCallVisitor(), null);
            
            //MethodCallVisitor mcv = new MethodCallVisitor();
            //n.getScope().accept(mcv, arg1);
        }
        /*
        System.out.print("MethodCallExpr: ");
        System.out.print(scope + " ");
        System.out.println(methodName);
		*/
        methodCalls.add(new MethodCall(scope, methodName));
                
        //super.visit(n, arg1);
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
}
