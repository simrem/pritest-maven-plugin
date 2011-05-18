package no.citrus.localprioritization;

import japa.parser.ParseException;
import no.citrus.localprioritization.algorithm.MethodCoverageAlgorithm;
import no.citrus.localprioritization.model.SummarizedTestCase;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;
import java.util.List;

public class TotalMethodCoverage extends MethodCoverage {

    private static Logger logger = Logger.getLogger(TotalMethodCoverage.class);

    public TotalMethodCoverage(String pathToProjectSource, String pathToTestSuite)
			throws ParseException, IOException {
        super();

        try {
            logger.addAppender(new FileAppender(new SimpleLayout(), "logs/TotalMethodCoverage.log"));
        } catch (IOException e1) {
        }

        prioritizeTestCases(pathToProjectSource, pathToTestSuite);
	}

    @Override
    protected void prioritizeTestCases(String pathToProjectSource, String pathToTestSuite)
    		throws ParseException, IOException {
    	super.prioritizeTestCases(pathToProjectSource, pathToTestSuite);
    	
        List<SummarizedTestCase> prioritizedTestCases = MethodCoverageAlgorithm.totalMethodCoverage(getTestSuiteMethodCoverage(), getSourceMethodCoverage());

        logger.info("Method calls found:");
        for (SummarizedTestCase stc : prioritizedTestCases) {
            addTestCase(stc);
            logTestCase(stc);
        }
    }

    private void logTestCase(SummarizedTestCase stc) {
        logger.info(stc.getTestCase().getPackageName() + "." + stc.getTestCase().getName() + " " + stc.getSummarizedCoverage().size());
    }
}
