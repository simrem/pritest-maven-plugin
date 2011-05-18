package no.citrus.localprioritization;

import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.visitor.CompilationUnitVisitor;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassTypeProvider {

    private static Logger logger = Logger.getLogger(ClassTypeProvider.class);

	private List<CompilationUnit> compilationUnits;
	private Map<String, ClassType> classTypeMap = new HashMap<String, ClassType>();
	
	public ClassTypeProvider(List<CompilationUnit> compilationUnits){
		this.compilationUnits = compilationUnits;

        try {
            logger.addAppender(new FileAppender(new SimpleLayout(), "logs/ClassTypeProvider.log"));
        } catch (IOException e1) {
        }

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
            	logger.warn("Unsupported java syntax", e);
            }
		}
	}

	public Map<String, ClassType> getClassTypes() {
		return this.classTypeMap;
	}

}
