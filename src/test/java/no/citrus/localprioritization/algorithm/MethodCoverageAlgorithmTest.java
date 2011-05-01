package no.citrus.localprioritization.algorithm;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.ProcessedMethodCall;
import no.citrus.localprioritization.model.ReferenceType;
import no.citrus.localprioritization.model.SummarizedTestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MethodCoverageAlgorithmTest {

	private Map<String,ClassCover> sourceMethodCoverage;
	private Map<String,ClassCover> testSuiteMethodCoverage;

	@Before
	public void setup() {
		setupTestSuite();
        setupSource();
	}

	private void setupSource() {
		sourceMethodCoverage = new HashMap<String, ClassCover>();
        
        ClassCover classA = new ClassCover("A", "no.citrus");
        List<ProcessedMethodCall> methodCallsAa = new ArrayList<ProcessedMethodCall>();
        MethodCover methodCoverAa = new MethodCover("A", "void", "a", new ArrayList<ReferenceType>(), methodCallsAa);
        classA.getMethods().put(MethodCover.createUniqueMapKey(methodCoverAa), methodCoverAa);
        sourceMethodCoverage.put(classA.getName(), classA);
        
        ClassCover classB = new ClassCover("B", "no.citrus");
        List<ProcessedMethodCall> methodCallsBb = new ArrayList<ProcessedMethodCall>();
        MethodCover methodCoverBb = new MethodCover("B", "void", "b", new ArrayList<ReferenceType>(), methodCallsBb);
        classB.getMethods().put(MethodCover.createUniqueMapKey(methodCoverBb), methodCoverBb);
        sourceMethodCoverage.put(classB.getName(), classB);
        
        ClassCover classC = new ClassCover("C", "no.citrus");
        List<ProcessedMethodCall> methodCallsCc = new ArrayList<ProcessedMethodCall>();
        MethodCover methodCoverCc = new MethodCover("C", "void", "c", new ArrayList<ReferenceType>(), methodCallsCc);
        classC.getMethods().put(MethodCover.createUniqueMapKey(methodCoverCc), methodCoverCc);
        sourceMethodCoverage.put(classC.getName(), classC);
        
        ClassCover classD = new ClassCover("D", "no.citrus");
        List<ProcessedMethodCall> methodCallsDd = new ArrayList<ProcessedMethodCall>();
        MethodCover methodCoverDd = new MethodCover("D", "void", "d", new ArrayList<ReferenceType>(), methodCallsDd);
        classD.getMethods().put(MethodCover.createUniqueMapKey(methodCoverDd), methodCoverDd);
        sourceMethodCoverage.put(classD.getName(), classD);
	}

	private void setupTestSuite() {
		testSuiteMethodCoverage = new HashMap<String, ClassCover>();
		
        ClassCover testCase1 = new ClassCover("test1", "no.citrus");
        MethodCover methodCover1A = new MethodCover("test1", "void", "should_do_1", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>());
        methodCover1A.getMethodCalls().add(new ProcessedMethodCall("A", "a", new ArrayList<String>()));
        methodCover1A.getMethodCalls().add(new ProcessedMethodCall("D", "d", new ArrayList<String>()));
        testCase1.getMethods().put(MethodCover.createUniqueMapKey(methodCover1A), methodCover1A);
		testSuiteMethodCoverage.put(testCase1.getName(), testCase1);
		
		ClassCover testCase2 = new ClassCover("test2", "no.citrus");
        MethodCover methodCover2A = new MethodCover("test2", "void", "should_do_2", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>());
        methodCover2A.getMethodCalls().add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
        methodCover2A.getMethodCalls().add(new ProcessedMethodCall("A", "a", new ArrayList<String>()));
        methodCover2A.getMethodCalls().add(new ProcessedMethodCall("D", "d", new ArrayList<String>()));
		testCase2.getMethods().put(MethodCover.createUniqueMapKey(methodCover2A), methodCover2A);
		testSuiteMethodCoverage.put(testCase2.getName(), testCase2);
		
		ClassCover testCase3 = new ClassCover("test3", "no.citrus");
        MethodCover methodCover3A = new MethodCover("test3", "void", "should_do_3", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>());
        methodCover3A.getMethodCalls().add(new ProcessedMethodCall("C", "c", new ArrayList<String>()));
		testCase3.getMethods().put(MethodCover.createUniqueMapKey(methodCover3A), methodCover3A);
		testSuiteMethodCoverage.put(testCase3.getName(), testCase3);
	}
	
    @Test
    @Ignore
    public void should_support_total_method_coverage() {
        List<SummarizedTestCase> totalMethodCoverage = MethodCoverageAlgorithm.totalMethodCoverage(testSuiteMethodCoverage, sourceMethodCoverage);
        
        assertThat(totalMethodCoverage.size(), is(equalTo(3)));
    }
}
