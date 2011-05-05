package no.citrus.localprioritization;

import japa.parser.ParseException;
import no.citrus.localprioritization.algorithm.MethodCoverageAlgorithm;
import no.citrus.localprioritization.model.SummarizedTestCase;

import java.io.IOException;
import java.util.List;

public class AdditionalMethodCoverage extends MethodCoverage {
	
    public AdditionalMethodCoverage(String pathToProjectSource, String pathToTestSuite) throws ParseException, IOException {
    	super();
    	prioritizeTestCases(pathToProjectSource, pathToTestSuite);
    }

	@Override
	protected void prioritizeTestCases(String pathToProjectSource,
			String pathToTestSuite) throws ParseException, IOException {
		super.prioritizeTestCases(pathToProjectSource, pathToTestSuite);
		
		List<SummarizedTestCase> prioritizedTestCases = MethodCoverageAlgorithm.additionalMethodCoverage(getTestSuiteMethodCoverage(), getSourceMethodCoverage());
		
		for (SummarizedTestCase stc : prioritizedTestCases) {
            addTestCase(stc);
//            System.out.println(stc.getTestCase().getPackageName() + "." + stc.getTestCase().getName() + " " + stc.getSummarizedCoverage().size());
        }
	}
}
