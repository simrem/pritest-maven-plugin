package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import no.citrus.localprioritization.model.MethodDecl;
import no.citrus.localprioritization.model.ReferenceType;

import java.util.ArrayList;
import java.util.List;


public class MethodDeclarationVisitor extends GenericVisitorAdapter<MethodDecl, Object> {
	
	@Override
	public MethodDecl visit(MethodDeclaration n, Object arg1) {
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
		
		List<ReferenceType> parameters = new ArrayList<ReferenceType>();
		
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				ParameterVisitor pv = new ParameterVisitor();
				p.accept(pv, null);
				parameters.add(new ReferenceType(pv.getParameterType(), pv.getParameterVariable()));
			}
		}
		
		return new MethodDecl(returnType, methodName, parameters);
	}

}
