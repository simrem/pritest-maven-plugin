package no.citrus.localprioritization.algorithm;

import no.citrus.localprioritization.MethodCoverageSummarizer;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.SummarizedTestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MethodCoverageAlgorithm {
    private static List<SummarizedTestCase> sortTestCasesByCoverage(Map<String, ClassCover> testSuiteMethodCoverage, Map<String, ClassCover> sourceMethodCoverage) {
		List<SummarizedTestCase> prioritizedTestCases = new ArrayList<SummarizedTestCase>();

	    Collection<ClassCover> testCaseCollection = testSuiteMethodCoverage.values();
	    for (ClassCover testCase : testCaseCollection) {
	        MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(sourceMethodCoverage, testCase);
	        Map<String, MethodCover> summarizedCoverage = mcs.getSummarizedCoverage();
	        SummarizedTestCase summarizedTestCase = new SummarizedTestCase(testCase, summarizedCoverage);
	        prioritizedTestCases.add(summarizedTestCase);
	    }

	    Collections.sort(prioritizedTestCases);
	    Collections.reverse(prioritizedTestCases);

		return prioritizedTestCases;
	}

    public static List<SummarizedTestCase> totalMethodCoverage(Map<String, ClassCover> testSuiteMethodCoverage, Map<String, ClassCover> sourceMethodCoverage) {
        return sortTestCasesByCoverage(testSuiteMethodCoverage, sourceMethodCoverage);
    }

    public static List<SummarizedTestCase> additionalMethodCoverage(Map<String, ClassCover> testSuiteMethodCoverage, Map<String, ClassCover> sourceMethodCoverage) {
        return sortTestCasesByCoverage(testSuiteMethodCoverage, sourceMethodCoverage);
    }
}
