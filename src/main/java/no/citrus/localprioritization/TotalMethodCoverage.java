package no.citrus.localprioritization;

import japa.parser.ParseException;
import no.citrus.localprioritization.algorithm.MethodCoverageAlgorithm;
import no.citrus.localprioritization.model.SummarizedTestCase;

import java.io.IOException;
import java.util.List;

public class TotalMethodCoverage extends MethodCoverage {

    public TotalMethodCoverage(String pathToProjectSource, String pathToTestSuite)
			throws ParseException, IOException {
        super();

        prioritizeTestCases(pathToProjectSource, pathToTestSuite);
	}

    @Override
    protected void prioritizeTestCases(String pathToProjectSource, String pathToTestSuite)
    		throws ParseException, IOException {
    	super.prioritizeTestCases(pathToProjectSource, pathToTestSuite);
    	
        List<SummarizedTestCase> prioritizedTestCases = MethodCoverageAlgorithm.totalMethodCoverage(getTestSuiteMethodCoverage(), getSourceMethodCoverage());

        for (SummarizedTestCase stc : prioritizedTestCases) {
            addTestCase(stc);
            System.out.println(stc.getTestCase().getPackageName() + "." + stc.getTestCase().getName() + " " + stc.getSummarizedCoverage().size());
        }
    }
}
