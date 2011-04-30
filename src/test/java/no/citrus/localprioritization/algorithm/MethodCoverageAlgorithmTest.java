package no.citrus.localprioritization.algorithm;

import no.citrus.localprioritization.model.ClassCover;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MethodCoverageAlgorithmTest {

    @Test
    public void should_support_total_method_coverage() {
        Map<String,ClassCover> testSuiteMethodCoverage = new HashMap<String, ClassCover>();

        Map<String,ClassCover> sourceMethodCoverage = new HashMap<String, ClassCover>();
        
        MethodCoverageAlgorithm.totalMethodCoverage(testSuiteMethodCoverage, sourceMethodCoverage);
    }
}
