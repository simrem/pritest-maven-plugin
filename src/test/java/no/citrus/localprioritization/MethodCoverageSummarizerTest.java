package no.citrus.localprioritization;

import java.util.HashMap;
import java.util.Map;

import no.citrus.localprioritization.model.ClassCover;

import org.junit.Test;

public class MethodCoverageSummarizerTest {
	
	@Test
	public void should_find_transitive_method_calls() {
		Map<String, ClassCover> coveredClasses = new HashMap<String, ClassCover>();
		
		ClassCover coveredTestCase = new ClassCover("ATest");
		
		MethodCoverageSummarizer mcs = new MethodCoverageSummarizer(coveredClasses, coveredTestCase);
		
	}
}
