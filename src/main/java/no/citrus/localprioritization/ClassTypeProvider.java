package no.citrus.localprioritization;

import japa.parser.ast.CompilationUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.visitor.CompilationUnitVisitor;

public class ClassTypeProvider {

	private List<CompilationUnit> compilationUnits;
	private Map<String, ClassType> classTypeMap = new HashMap<String, ClassType>();
	
	public ClassTypeProvider(List<CompilationUnit> compilationUnits){
		this.compilationUnits = compilationUnits;
		retrieveClassTypes();
	}
	
	private void retrieveClassTypes() {
		CompilationUnitVisitor cuv;
		for(CompilationUnit cu : compilationUnits){
            try {
                cuv = new CompilationUnitVisitor();
                cu.accept(cuv, null);
                classTypeMap.putAll(cuv.getTypesAsMapItems());
            } catch (Exception e) {
                //  TODO: should log errors for later analysis
            }
		}
	}

	public Map<String, ClassType> getClassTypes() {
		return this.classTypeMap;
	}

}
