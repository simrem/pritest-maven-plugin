package no.citrus.localprioritization;

import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;

public class MethodCoverageSummarizer {

	private final Map<String, ClassCover> coveredClasses;
	private final ClassCover coveredTestCase;

	public MethodCoverageSummarizer(Map<String, ClassCover> coveredClasses, ClassCover coveredTestCase) {
		this.coveredClasses = coveredClasses;
		this.coveredTestCase = coveredTestCase;
	}

}
