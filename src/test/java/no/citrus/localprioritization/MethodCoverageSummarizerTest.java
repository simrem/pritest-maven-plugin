package no.citrus.localprioritization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.ProcessedMethodCall;
import no.citrus.localprioritization.model.ReferenceType;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCoverageSummarizerTest {
	
	@Test
	@Ignore
	public void should_find_transitive_method_calls() {
		Map<String, ClassCover> coveredClasses = new HashMap<String, ClassCover>();
		
		ClassCover coveredClassA = new ClassCover("A");
		List<ProcessedMethodCall> methodCallsA = new ArrayList<ProcessedMethodCall>();
		methodCallsA.add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
		coveredClassA.getMethods().put("a", new MethodCover("A", "void", "a", new ArrayList<ReferenceType>(), methodCallsA));
		coveredClasses.put("A", coveredClassA);
		
		ClassCover coveredClassB = new ClassCover("B");
		coveredClassB.getMethods().put("b", new MethodCover("B", "void", "b", 
				new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>()));
		coveredClasses.put("B", coveredClassB);
		
		ClassCover coveredTestCase = new ClassCover("ATest");
		
		MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(coveredClasses);
		ClassCover summarizedCoverageOfTestCase = mcs.summarizeCoverageOfTestCase(coveredTestCase);
		
		List<ProcessedMethodCall> answerMethodCalls = new ArrayList<ProcessedMethodCall>();
		answerMethodCalls.add(new ProcessedMethodCall("B", "b", new ArrayList<String>()));
		
		assertThat(summarizedCoverageOfTestCase.getMethods().values(), hasItems(
				new MethodCover("A", "void", "a", new ArrayList<ReferenceType>(), answerMethodCalls),
				new MethodCover("B", "void", "b", new ArrayList<ReferenceType>(), new ArrayList<ProcessedMethodCall>())
		));
	}
}
