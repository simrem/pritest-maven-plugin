package no.citrus.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodCover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MethodCoverage {
    protected List<String> testCases;
	private Map<String, ClassCover> sourceMethodCoverage;
	private Map<String, ClassCover> testSuiteMethodCoverage;
	private Map<String, ClassType> projectSourceClassTypes;

    public MethodCoverage() {
        testCases = new ArrayList<String>();
    }
    
    protected void retrieveCoverage(String pathToProjectSource, String pathToTestSuite)
			throws ParseException, IOException {
		retrieveClassCoverage(pathToProjectSource);
		retrieveTestClassCoverage(pathToTestSuite);
	}

    private void retrieveClassCoverage(String pathToProjectSource)
			throws ParseException, IOException {

		List<File> fileList =
			ClassListProvider.getFileList(new File(pathToProjectSource), new String[] {".java"});

		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(fileList);

		ClassTypeProvider classTypeProvider = new ClassTypeProvider(compilationUnits);
		projectSourceClassTypes = classTypeProvider.getClassTypes();

		MethodCoverageProvider mcp = new MethodCoverageProvider(projectSourceClassTypes, compilationUnits);

		sourceMethodCoverage = mcp.getMethodCoverage();
	}
    
    private void retrieveTestClassCoverage(String pathToTestSuiteSource)
			throws ParseException, IOException {
	
		List<File> fileList =
			ClassListProvider.getFileList(new File(pathToTestSuiteSource), new String[] {".java"});
		
		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(fileList);
		
		ClassTypeProvider classTypeProvider = new ClassTypeProvider(compilationUnits);
		Map<String, ClassType> classTypes = classTypeProvider.getClassTypes();
		
		Map<String, ClassType> projectSourceAndTestSuite = new HashMap<String, ClassType>();
		projectSourceAndTestSuite.putAll(classTypes);
		projectSourceAndTestSuite.putAll(projectSourceClassTypes);
		
		MethodCoverageProvider mcp = new MethodCoverageProvider(projectSourceAndTestSuite, compilationUnits);
		
		testSuiteMethodCoverage = mcp.getMethodCoverage();
	}
    
    public List<String> getTestCases() {
		return testCases;
	}
    
    public Map<String, ClassCover> getSourceMethodCoverage() {
		return sourceMethodCoverage;
	}
    
	public Map<String, ClassCover> getTestSuiteMethodCoverage() {
		return testSuiteMethodCoverage;
	}

	protected class SummarizedTestCase implements Comparable<TotalMethodCoverage.SummarizedTestCase> {

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

        public int compareTo(TotalMethodCoverage.SummarizedTestCase summarizedTestCase) {
            Integer sizeOfThis = new Integer(summarizedCoverage.size());
            return sizeOfThis.compareTo(new Integer(summarizedTestCase.getSummarizedCoverage().size()));
        }
    }
}
