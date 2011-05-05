package no.citrus.localprioritization.model;

import java.util.HashMap;
import java.util.Map;

public class SummarizedTestCase implements Comparable<SummarizedTestCase> {

    private ClassCover testCase;
    private Map<String, MethodCover> summarizedCoverage;
    private Map<String, Boolean> covered;

    public SummarizedTestCase(ClassCover testCase, Map<String, MethodCover> summarizedCoverage) {
        this.testCase = testCase;
        this.summarizedCoverage = summarizedCoverage;
        this.covered = new HashMap<String, Boolean>();
        
        for (MethodCover coveredMethod : summarizedCoverage.values()) {
        	this.covered.put(MethodCover.createUniqueMapKey(coveredMethod), true);
        }
    }

    public ClassCover getTestCase() {
        return testCase;
    }

    public Map<String, MethodCover> getSummarizedCoverage() {
        return summarizedCoverage;
    }

    public Map<String, Boolean> getCovered() {
		return covered;
	}

	public int compareTo(SummarizedTestCase summarizedTestCase) {
		Integer sizeOfThis = new Integer(this.coveredMethods());
		return sizeOfThis.compareTo(new Integer(summarizedTestCase.coveredMethods()));
    }
	
	public int coveredMethods() {
		int coveredMethods = 0;
		for (Boolean method : covered.values()) {
			if (method.equals(Boolean.TRUE)) {
				coveredMethods++;
			}
		}
		return coveredMethods;
	}

	public void markMethod(MethodCover coveredMethod) {
		if (covered.get(MethodCover.createUniqueMapKey(coveredMethod)) != null) {
			covered.put(MethodCover.createUniqueMapKey(coveredMethod), false);
		}
	}
	
	public void unMarkMethod(MethodCover coveredMethod) {
		if (covered.get(MethodCover.createUniqueMapKey(coveredMethod)) != null) {
			covered.put(MethodCover.createUniqueMapKey(coveredMethod), true);
		}
	}
}
