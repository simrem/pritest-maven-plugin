package no.citrus.localprioritization;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.ProcessedMethodCall;
import no.citrus.localprioritization.model.ReferenceType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCoverageSummarizerTest {
	
	private Map<String, MethodCover> summarizedCoverageOfTestCase;

	@Before
	public void setup() {
		Map<String, ClassCover> coveredClasses = new HashMap<String, ClassCover>();
		
		ClassCover coveredClassA = new ClassCover("A", null);
		List<ProcessedMethodCall> methodCallsA = new ArrayList<ProcessedMethodCall>();
		methodCallsA.add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
		methodCallsA.add(new ProcessedMethodCall("C", "c", new ArrayList<String>()));
        MethodCover methodCoverAa = new MethodCover("A", "void", "a", new ArrayList<ReferenceType>(), methodCallsA);
		coveredClassA.getMethods().put(MethodCover.createUniqueMapKey(methodCoverAa), methodCoverAa);
		coveredClasses.put("A", coveredClassA);
		
		ClassCover coveredClassB = new ClassCover("B", null);
		List<ProcessedMethodCall> methodCallsB = new ArrayList<ProcessedMethodCall>();
		methodCallsB.add(new ProcessedMethodCall("D", "d", new ArrayList<String>()));
        MethodCover methodCoverBb = new MethodCover("B", "void", "b", new ArrayList<ReferenceType>(), methodCallsB);
		coveredClassB.getMethods().put(MethodCover.createUniqueMapKey(methodCoverBb), methodCoverBb);
		coveredClasses.put("B", coveredClassB);
		
		ClassCover coveredClassC = new ClassCover("C", null);
        MethodCover methodCoverCc = new MethodCover("C", "void", "c", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>());
		coveredClassC.getMethods().put(MethodCover.createUniqueMapKey(methodCoverCc), methodCoverCc);
		coveredClasses.put("C", coveredClassC);
		
		ClassCover coveredClassD = new ClassCover("D", null);
        MethodCover methodCoverDd = new MethodCover("D", "void", "d", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>());
		coveredClassD.getMethods().put(MethodCover.createUniqueMapKey(methodCoverDd), methodCoverDd);
		coveredClasses.put("D", coveredClassD);
		
		ClassCover coveredTestCase = new ClassCover("ATest", null);
		List<ProcessedMethodCall> methodsCoveredByTest = new ArrayList<ProcessedMethodCall>();
		methodsCoveredByTest.add(new ProcessedMethodCall("A", "a", new ArrayList<String>()));
        MethodCover methodCoverATest = new MethodCover("ATest", "void", "should_bla_bla", new ArrayList<ReferenceType>(), methodsCoveredByTest);
		coveredTestCase.getMethods().put(MethodCover.createUniqueMapKey(methodCoverATest), methodCoverATest);
		
		MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(coveredClasses, coveredTestCase);
		summarizedCoverageOfTestCase = mcs.getSummarizedCoverage();
	}
	
	@Test
	public void should_find_direct_method_calls() {
		List<ProcessedMethodCall> answerMethodCallsA = new ArrayList<ProcessedMethodCall>();
		answerMethodCallsA.add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
		answerMethodCallsA.add(new ProcessedMethodCall("C", "c", new ArrayList<String>()));
		
		assertThat(summarizedCoverageOfTestCase.values(), hasItems(
				new MethodCover("A", "void", "a", new ArrayList<ReferenceType>(), answerMethodCallsA)
		));
	}
	
	@Test
	public void should_find_parallel_method_calls() {
		assertThat(summarizedCoverageOfTestCase.values(), hasItems(
				new MethodCover("C", "void", "c", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>())
		));
	}
	
	@Test
	public void should_find_transitive_method_calls() {
		assertThat(summarizedCoverageOfTestCase.values(), hasItems(
				new MethodCover("D", "void", "d", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>())
		));
	}
	
	@Test
	public void should_not_include_test_methods() {
		List<ProcessedMethodCall> testCaseMethodCalls = new ArrayList<ProcessedMethodCall>();
		testCaseMethodCalls.add(new ProcessedMethodCall("A", "a", new ArrayList<String>()));
		
		assertThat(summarizedCoverageOfTestCase.values(), not(hasItems(
				new MethodCover("ATest", "void", "should_bla_bla", new ArrayList<ReferenceType>(), testCaseMethodCalls)
		)));
	}
}
