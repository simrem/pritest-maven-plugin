package no.citrus.runner.junit.priority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.errors.NoWorkTreeException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class Technique4RankerTest {
	
	private static List<String> localTestClasses = new ArrayList<String>();
	private static Technique4Ranker t4;

	@BeforeClass
	public static void beforeClass() {
		localTestClasses.add("classATest");
		localTestClasses.add("classBTest");
		localTestClasses.add("classCTest");
	}
	
	@Before
	public void before() {
		t4 = new Technique4Ranker(localTestClasses);
	}
	
	@Test
	public void should_instantiate_with_elements_from_localTestClasses() {
		assertThat(t4.getLocalTestClasses().get(0), equalTo("classATest"));
		assertThat(t4.getLocalTestClasses().get(1), equalTo("classBTest"));
		assertThat(t4.getLocalTestClasses().get(2), equalTo("classCTest"));
	}
	
	@Test
	public void should_contain_a_list_of_untracked_modified_and_local_test_classes() throws IOException {
		List<String> gitStatusFiles = t4.getTechnique4PriorityList();
		
		for (String s : gitStatusFiles) {
			System.out.println(s + "\n");
		}
	}
}
