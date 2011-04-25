package no.citrus.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodCover;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TotalMethodCoverage {
	private List<ClassCover> testCases;

	public TotalMethodCoverage(String pathToProjectSource, String pathToTestSuite)
			throws ParseException, IOException {

        testCases = new ArrayList<ClassCover>();

        prioritizeTestCases(pathToProjectSource, pathToTestSuite);
	}

    private void prioritizeTestCases(String pathToProjectSource, String pathToTestSuite) throws ParseException, IOException {
        Map<String, ClassCover> sourceClassCover = retrieveClassCoverage(pathToProjectSource);
        Map<String, ClassCover> testClassCover = retrieveClassCoverage(pathToTestSuite);

        List<SummarizedTestCase> prioritizedTestCases = new ArrayList<SummarizedTestCase>();

        Collection<ClassCover> testCaseCollection = testClassCover.values();
        for (ClassCover testCase : testCaseCollection) {
            MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(sourceClassCover, testCase);
            Map<String, MethodCover> summarizedCoverage = mcs.getSummarizedCoverage();
            SummarizedTestCase summarizedTestCase = new SummarizedTestCase(testCase, summarizedCoverage);
            prioritizedTestCases.add(summarizedTestCase);
        }

        Collections.sort(prioritizedTestCases);

        for (SummarizedTestCase stc : prioritizedTestCases) {
            testCases.add(stc.getTestCase());
            System.out.println(stc.getTestCase().getName() + " " + stc.getSummarizedCoverage().size());
        }
    }

    private Map<String, ClassCover> retrieveClassCoverage(String pathToProjectSource)
			throws ParseException, IOException {
		
		List<File> fileList = 
			ClassListProvider.getFileList(new File(pathToProjectSource), new String[] {".java"});
		
		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(fileList);
		
		ClassTypeProvider classTypeProvider = new ClassTypeProvider(compilationUnits);
		Map<String, ClassType> classTypes = classTypeProvider.getClassTypes();
		
		MethodCoverageProvider mcp = new MethodCoverageProvider(classTypes, compilationUnits);
		
		return mcp.getMethodCoverage();
	}

	public List<ClassCover> getTestCases() {
		return testCases;
	}

    private class SummarizedTestCase implements Comparable<SummarizedTestCase> {

        private ClassCover testCase;
        private Map<String, MethodCover> summarizedCoverage;

        public SummarizedTestCase(ClassCover testCase, Map<String, MethodCover> summarizedCoverage) {

            this.testCase = testCase;
            this.summarizedCoverage = summarizedCoverage;
        }

        public ClassCover getTestCase() {
            return testCase;
        }

        public Map<String, MethodCover> getSummarizedCoverage() {
            return summarizedCoverage;
        }

        public int compareTo(SummarizedTestCase summarizedTestCase) {
            Integer sizeOfThis = new Integer(summarizedCoverage.size());
            return sizeOfThis.compareTo(new Integer(summarizedTestCase.getSummarizedCoverage().size()));
        }
    }
}
