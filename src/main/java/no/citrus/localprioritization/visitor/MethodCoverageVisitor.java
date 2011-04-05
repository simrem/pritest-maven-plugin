package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.MethodDecl;

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

        coveredClasses.put(currentClass.getClassName(), currentClass);
    }

    @Override
    public void visit(MethodDeclaration n, ClassCover arg) {
        MethodDeclarationVisitor mdv = new MethodDeclarationVisitor();
		n.accept(mdv, null);

		List<MethodDecl> methodDeclarations = mdv.getMethodDeclarations();
        for (MethodDecl md : methodDeclarations) {
            String returnType = md.getReturnType();
            String methodName = md.getMethodName();
            arg.getMethods().put(returnType + "." + methodName, new MethodCover(returnType, methodName));
        }
    }

    public Map<String,ClassCover> getCoveredClasses() {
        return coveredClasses;
    }
}
