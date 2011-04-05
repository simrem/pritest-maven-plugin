package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;

import java.util.HashMap;
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
        System.out.println("- ClassOrInterfaceDeclaration: " + n.getName());

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

    }

    public Map<String,ClassCover> getCoveredClasses() {
        return coveredClasses;
    }
}
