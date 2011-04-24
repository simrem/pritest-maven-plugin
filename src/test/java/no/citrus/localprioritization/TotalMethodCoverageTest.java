package no.citrus.localprioritization;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.List;

import no.citrus.localprioritization.model.ClassCover;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class TotalMethodCoverageTest {
	
	@Test
	@Ignore
	public void should_give_a_list_of_test_cases_with_size_greater_than_zero() throws ParseException, IOException {
		TotalMethodCoverage tmc = new TotalMethodCoverage("src/main/java", "src/test/java");
		List<ClassCover> testCases = tmc.getTestCases();
		
		assertThat(testCases.size(), is(not(0)));
	}
}
