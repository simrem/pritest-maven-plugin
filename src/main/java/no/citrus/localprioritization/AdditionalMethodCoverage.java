package no.citrus.localprioritization;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.MethodCover;

public class AdditionalMethodCoverage extends MethodCoverage {
	
    public AdditionalMethodCoverage(String pathToProjectSource, String pathToTestSuite) throws ParseException, IOException {
    	super();
    	prioritizeTestCases(pathToProjectSource, pathToTestSuite);
    }

	@Override
	protected void prioritizeTestCases(String pathToProjectSource,
			String pathToTestSuite) throws ParseException, IOException {
		super.prioritizeTestCases(pathToProjectSource, pathToTestSuite);
		
		List<SummarizedTestCase> prioritizedTestCases = sortTestCasesByCoverage();
		
		while (!prioritizedTestCases.isEmpty()) {
			SummarizedTestCase summarizedTestCase = prioritizedTestCases.remove(0);
			addTestCase(summarizedTestCase);
			
			Map<String, MethodCover> alreadyCoveredMethods = summarizedTestCase.getSummarizedCoverage();
			
			for (MethodCover coveredMethod : alreadyCoveredMethods.values()) {
				for (SummarizedTestCase stc : prioritizedTestCases) {
					stc.getSummarizedCoverage().remove(coveredMethod.getClassName() + "." + coveredMethod.getMethodName());
				}
			}
			
			Collections.sort(prioritizedTestCases);
			Collections.reverse(prioritizedTestCases);
			
//			System.out.println(summarizedTestCase.getTestCase().getPackageName() + "." 
//					+ summarizedTestCase.getTestCase().getName() + " "
//					+ summarizedTestCase.getSummarizedCoverage().size());
		}
	}
}
