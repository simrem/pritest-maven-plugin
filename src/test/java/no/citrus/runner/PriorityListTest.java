package no.citrus.runner;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import no.citrus.runner.junit.priority.ClassService;
import no.citrus.runner.junit.priority.PriorityList;

import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class PriorityListTest {
	
	private class TestClassService implements ClassService{
		private final List<String> list;
		public TestClassService(List<String> list){
			this.list = list;
		}

		@Override
		public List<String> getClassList() throws Exception, JSONException,
				ConnectException {
			return this.list;
		}
	}
	
	private TestClassService localClassList;
	private TestClassService onlineClassList;
	private List<String> expectedResult;
	
	@Before
	public void setupEnvironment(){
		ArrayList<String> localList = new ArrayList<String>();
		localList.add("no.test.ClassA");
		localList.add("no.test.ClassB");
		localList.add("no.test.ClassC");
		localList.add("no.test.ClassD");
		localClassList = new TestClassService(localList);
		
		ArrayList<String> onlineList = new ArrayList<String>();
		onlineList.add("no.test.ClassD");
		onlineList.add("no.test.ClassB");
		onlineClassList = new TestClassService(onlineList);
		
		expectedResult = new ArrayList<String>();
		expectedResult.add("no.test.ClassA");
		expectedResult.add("no.test.ClassC");
		expectedResult.add("no.test.ClassD");
		expectedResult.add("no.test.ClassB");
	}
	
	@Test
	public void shouldReturnListInCorrectOrder() throws JSONException, Exception{
		PriorityList priorityList = new PriorityList(onlineClassList, localClassList);
		assertThat(priorityList.getPriorityList(), equalTo(expectedResult));
	}
}
