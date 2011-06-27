/**
    This file is part of Pritest.

    Pritest is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Pritest is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package no.pritest.runner;

import no.pritest.runner.junit.priority.ClassService;
import no.pritest.runner.junit.priority.PriorityList;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class PriorityListTest {
	
	private class TestClassService implements ClassService {
		private final List<String> list;
		public TestClassService(List<String> list){
			this.list = list;
		}

		public List<String> getClassList() throws Exception, JSONException,
				ConnectException {
			return this.list;
		}
	}
	
	private TestClassService localClassList;
	private TestClassService onlineClassList;
	private List<String> expectedResult;
	
	@Before
	public void setup_environment(){
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
	public void should_return_list_in_correct_order() throws JSONException, Exception{
		PriorityList priorityList = new PriorityList(onlineClassList, localClassList);
		assertThat(priorityList.getPriorityList(), equalTo(expectedResult));
	}
}
