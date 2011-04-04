package no.citrus.localprioritization.visitor;

import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;

import java.util.HashMap;
import java.util.Map;

public class MethodCoverageVisitor extends VoidVisitorAdapter<Object> {
    private Map<String, ClassCover> coveredClasses;
    private Map<String, ClassType> classesInProject;

    public MethodCoverageVisitor(Map<String, ClassType> classesInProject) {
        coveredClasses = new HashMap<String, ClassCover>();
        this.classesInProject = classesInProject;
    }

    public Map<String,ClassCover> getCoveredClasses() {
        return coveredClasses;
    }
}
