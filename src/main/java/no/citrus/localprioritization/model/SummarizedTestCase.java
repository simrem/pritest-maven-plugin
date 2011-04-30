package no.citrus.localprioritization.model;

import java.util.Map;

public class SummarizedTestCase implements Comparable<SummarizedTestCase> {

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
