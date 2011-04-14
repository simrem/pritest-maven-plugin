package no.citrus.localprioritization;

import java.util.HashMap;
import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;

public class MethodCoverageSummarizer {

	private final Map<String, ClassCover> coveredClasses;

	public MethodCoverageSummarizer(Map<String, ClassCover> coveredClasses) {
		this.coveredClasses = coveredClasses;
	}

	public ClassCover summarizeCoverageOfTestCase(ClassCover coveredTestCase) {
		Map<String, MethodCover> methods = coveredTestCase.getMethods();
		
		Map<String, MethodCover> transitiveMethods = new HashMap<String, MethodCover>();
		
		//for ()
		
		return coveredTestCase;
	}

}
