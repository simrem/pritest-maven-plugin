package no.citrus.localprioritization;

import japa.parser.ParseException;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;

import java.io.IOException;
import java.util.*;

public class TotalMethodCoverage extends MethodCoverage {

    public TotalMethodCoverage(String pathToProjectSource, String pathToTestSuite)
			throws ParseException, IOException {
        super();

        prioritizeTestCases(pathToProjectSource, pathToTestSuite);
	}

    private void prioritizeTestCases(String pathToProjectSource, String pathToTestSuite) throws ParseException, IOException {
    	retrieveCoverage(pathToProjectSource, pathToTestSuite);
    	
        Map<String, ClassCover> sourceClassCover = getSourceMethodCoverage();
        Map<String, ClassCover> testClassCover = getTestSuiteMethodCoverage();

        List<SummarizedTestCase> prioritizedTestCases = new ArrayList<SummarizedTestCase>();

        Collection<ClassCover> testCaseCollection = testClassCover.values();
        for (ClassCover testCase : testCaseCollection) {
            MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(sourceClassCover, testCase);
            Map<String, MethodCover> summarizedCoverage = mcs.getSummarizedCoverage();
            SummarizedTestCase summarizedTestCase = new SummarizedTestCase(testCase, summarizedCoverage);
            prioritizedTestCases.add(summarizedTestCase);
        }

        Collections.sort(prioritizedTestCases);
        Collections.reverse(prioritizedTestCases);

        for (SummarizedTestCase stc : prioritizedTestCases) {
        	ClassCover testCase = stc.getTestCase();
            testCases.add(testCase.getPackageName() + "." + testCase.getName());
            System.out.println(testCase.getPackageName() + "." + testCase.getName() + " " + stc.getSummarizedCoverage().size());
        }
    }

}
