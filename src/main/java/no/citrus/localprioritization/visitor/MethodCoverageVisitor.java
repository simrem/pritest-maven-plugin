package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCoverageVisitor extends VoidVisitorAdapter<ClassCover> {
    private Map<String, ClassCover> coveredClasses;
    private Map<String, ClassType> classesInProject;

    public MethodCoverageVisitor(Map<String, ClassType> classesInProject) {
        coveredClasses = new HashMap<String, ClassCover>();
        this.classesInProject = classesInProject;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, ClassCover arg) {
        ClassCover currentClass = new ClassCover(n.getName());

        if (n.getMembers() != null) {
            for (BodyDeclaration member : n.getMembers()) {
                member.accept(this, currentClass);
            }
        }

        coveredClasses.put(currentClass.getName(), currentClass);
    }

    @Override
    public void visit(MethodDeclaration n, ClassCover arg) {
        MethodDecl methodDeclaration = n.accept(new MethodDeclarationVisitor(), null);
    	
    	String returnType = methodDeclaration.getReturnType();
        String methodName = methodDeclaration.getMethodName();
        List<ReferenceType> parameters = methodDeclaration.getParameters();
        
        List<ProcessedMethodCall> methodCalls = new ArrayList<ProcessedMethodCall>();

        if (n.getBody() != null) {
        	Map<String, ReferenceType> localVariables = extractLocalVariables(n);
        	Map<String, ReferenceType> fieldVariables = retrieveFieldVariablesOfCurrentClass(arg);
        	
            MethodCallVisitor mcv = new MethodCallVisitor(localVariables, fieldVariables);
            n.getBody().accept(mcv, null);
            
            for (RawMethodCall rawMethodCall : mcv.getRawMethodCalls()) {
            	ProcessedMethodCall processedMethodCall = processRawMethodCall(localVariables, rawMethodCall);
            	if (processedMethodCall != null) {
            		methodCalls.add(processedMethodCall);
            	}
            }
        }

        arg.getMethods().put(methodName,
        		new MethodCover(arg.getName(), returnType, methodName, parameters, methodCalls));
    }

	private Map<String, ReferenceType> retrieveFieldVariablesOfCurrentClass(
			ClassCover arg) {
		ClassType currentClass = classesInProject.get(arg.getName());
		Map<String, ReferenceType> fieldVariables =
			(currentClass != null ? currentClass.getFields() : new HashMap<String, ReferenceType>());
		return fieldVariables;
	}
    
    private Map<String, ReferenceType> extractLocalVariables(MethodDeclaration n) {
    	VariableDeclarationVisitor vdv = new VariableDeclarationVisitor();
    	n.getBody().accept(vdv, null);
    	
    	Map<String, ReferenceType> localVariables = vdv.getVariables();
    	
    	return localVariables;
    }
    
    private ProcessedMethodCall processRawMethodCall(Map<String, ReferenceType> localVariables, RawMethodCall rawMethodCall) {
    	ReferenceType variable = localVariables.get(rawMethodCall.getScope());
        if (variable != null) {
            if (isClassInProject(variable)) {
                return new ProcessedMethodCall(variable.getType(), rawMethodCall.getMethodName(), rawMethodCall.getParameters());
            }
        } else {
        	//variable = classesInProject.get()
        }
        
        return null;
    }
    
    private boolean isClassInProject(ReferenceType variable) {
        return classesInProject.get(variable.getType()) != null;
    }

    public Map<String,ClassCover> getCoveredClasses() {
        return coveredClasses;
    }
}
