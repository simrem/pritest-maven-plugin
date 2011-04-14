package no.citrus.localprioritization;

import japa.parser.ast.CompilationUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.visitor.MethodCoverageVisitor;

public class MethodCoverageProvider {

	private final Map<String, ClassType> classTypes;
	private Map<String, ClassCover> classCovers;
	private final List<CompilationUnit> compilationUnits;
	
	
	public MethodCoverageProvider(Map<String, ClassType> classTypes, List<CompilationUnit> compilationUnits) {
		
		this.classTypes = classTypes;
		this.compilationUnits = compilationUnits;
		retrieveMethodCoverage();
	}

	private void retrieveMethodCoverage() {
		MethodCoverageVisitor mcv;
		classCovers = new HashMap<String, ClassCover>();
		for(CompilationUnit cu : compilationUnits){
			mcv = new MethodCoverageVisitor(classTypes);
			cu.accept(mcv, null);
			classCovers.putAll(mcv.getCoveredClasses());
		}
	}

	public Map<String, ClassCover> getMethodCoverage() {
		return classCovers;
	}

}
