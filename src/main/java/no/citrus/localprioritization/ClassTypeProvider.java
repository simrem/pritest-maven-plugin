package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.visitor.CompilationUnitVisitor;

public class ClassTypeProvider {

	private List<File> projectFiles;
	private Map<String, ClassType> classTypeMap = new HashMap<String, ClassType>();
	
	public ClassTypeProvider(List<File> projectFiles) throws ParseException, IOException {
		this.projectFiles = projectFiles;
		retrieveClassTypes();
	}
	
	private void retrieveClassTypes() throws ParseException, IOException {
		CompilationUnitVisitor cuv;
		CompilationUnit cu;
		for(File javaFile : this.projectFiles){
			cu = JavaParser.parse(javaFile);
			cuv = new CompilationUnitVisitor();
			cu.accept(cuv, null);
			classTypeMap.putAll(cuv.getTypesAsMapItems());
		}
	}

	public List<File> getProjectFiles(){
		return projectFiles;
	}

	public Map<String, ClassType> getClassTypes() {
		return this.classTypeMap;
	}

}
