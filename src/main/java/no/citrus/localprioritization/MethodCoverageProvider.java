package no.citrus.localprioritization;

import japa.parser.ast.CompilationUnit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.visitor.MethodCoverageVisitor;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class MethodCoverageProvider {

    private static Logger logger = Logger.getLogger(MethodCoverageProvider.class);

	private final Map<String, ClassType> classTypes;
	private Map<String, ClassCover> classCovers;
	private final List<CompilationUnit> compilationUnits;
	
	public MethodCoverageProvider(Map<String, ClassType> classTypes, List<CompilationUnit> compilationUnits) {
		this.classTypes = classTypes;
		this.compilationUnits = compilationUnits;

        try {
            logger.addAppender(new FileAppender(new SimpleLayout(), "logs/MethodCoverageProvider.log"));
        } catch (IOException e1) {
        }

		retrieveMethodCoverage();
	}

	private void retrieveMethodCoverage() {
		MethodCoverageVisitor mcv;
		classCovers = new HashMap<String, ClassCover>();
		for(CompilationUnit cu : compilationUnits){
            try {
                mcv = new MethodCoverageVisitor(classTypes);
                cu.accept(mcv, null);
                classCovers.putAll(mcv.getCoveredClasses());
            } catch (Exception e) {
//                logger.warn("Unsupported java syntax");
                logger.warn("Unsupported java syntax", e);
//                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
//                    logger.warn(stackTraceElement.toString());
//                }
            }
		}
	}

	public Map<String, ClassCover> getMethodCoverage() {
		return classCovers;
	}

}
