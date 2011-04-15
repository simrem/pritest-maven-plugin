package no.citrus.localprioritization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.ProcessedMethodCall;
import no.citrus.localprioritization.model.ReferenceType;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class MethodCoverageSummarizerTest {
	
	private Map<String, MethodCover> summarizedCoverageOfTestCase;

	@Before
	public void setup() {
		Map<String, ClassCover> coveredClasses = new HashMap<String, ClassCover>();
		
		ClassCover coveredClassA = new ClassCover("A");
		List<ProcessedMethodCall> methodCallsA = new ArrayList<ProcessedMethodCall>();
		methodCallsA.add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
		methodCallsA.add(new ProcessedMethodCall("C", "c", new ArrayList<String>()));
		coveredClassA.getMethods().put("a", new MethodCover("A", "void", "a",
				new ArrayList<ReferenceType>(), methodCallsA));
		coveredClasses.put("A", coveredClassA);
		
		ClassCover coveredClassB = new ClassCover("B");
		List<ProcessedMethodCall> methodCallsB = new ArrayList<ProcessedMethodCall>();
		methodCallsB.add(new ProcessedMethodCall("D", "d", new ArrayList<String>()));
		coveredClassB.getMethods().put("b", new MethodCover("B", "void", "b", 
				new ArrayList<ReferenceType>(), methodCallsB));
		coveredClasses.put("B", coveredClassB);
		
		ClassCover coveredClassC = new ClassCover("C");
		coveredClassC.getMethods().put("c", new MethodCover("C", "void", "c", 
				new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>()));
		coveredClasses.put("C", coveredClassC);
		
		ClassCover coveredClassD = new ClassCover("D");
		coveredClassD.getMethods().put("d", new MethodCover("D", "void", "d", 
				new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>()));
		coveredClasses.put("D", coveredClassC);
		
		ClassCover coveredTestCase = new ClassCover("ATest");
		List<ProcessedMethodCall> methodsCoveredByTest = new ArrayList<ProcessedMethodCall>();
		methodsCoveredByTest.add(new ProcessedMethodCall("A", "a", new ArrayList<String>()));
		coveredTestCase.getMethods().put("should_bla_bla", new MethodCover("ATest", "void", "should_bla_bla",
				new ArrayList<ReferenceType>(), methodsCoveredByTest));
		
		MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(coveredClasses, coveredTestCase);
		summarizedCoverageOfTestCase = mcs.getSummarizedCoverage();
	}
	
	@Test
	public void should_find_direct_method_calls() {
		List<ProcessedMethodCall> answerMethodCallsA = new ArrayList<ProcessedMethodCall>();
		answerMethodCallsA.add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
		answerMethodCallsA.add(new ProcessedMethodCall("C", "c", new ArrayList<String>()));
		
		assertThat(summarizedCoverageOfTestCase.values(), hasItems(
				new MethodCover("A", "void", "a", new ArrayList<ReferenceType>(), answerMethodCallsA),
				new MethodCover("B", "void", "b", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>())
		));
	}
	
	@Test
	public void should_find_parallell_method_calls() {
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
