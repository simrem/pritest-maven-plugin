package no.citrus.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodCover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class MethodCoverage {
    protected List<String> testCases;

    public MethodCoverage() {
        testCases = new ArrayList<String>();
    }

    protected Map<String, ClassCover> retrieveClassCoverage(String pathToProjectSource)
			throws ParseException, IOException {

		List<File> fileList =
			ClassListProvider.getFileList(new File(pathToProjectSource), new String[] {".java"});

		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(fileList);

		ClassTypeProvider classTypeProvider = new ClassTypeProvider(compilationUnits);
		Map<String, ClassType> classTypes = classTypeProvider.getClassTypes();

		MethodCoverageProvider mcp = new MethodCoverageProvider(classTypes, compilationUnits);

		return mcp.getMethodCoverage();
	}

    public List<String> getTestCases() {
		return testCases;
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
