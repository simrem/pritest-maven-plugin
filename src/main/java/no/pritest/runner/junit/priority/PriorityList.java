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

package no.pritest.runner.junit.priority;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;

public class PriorityList {
	
	private final ClassService localClassService;
	private final ClassService onlineClassService;
	
	public PriorityList(ClassService onlineClassService, ClassService localClassService){
		this.localClassService = localClassService;
		this.onlineClassService = onlineClassService;
	}
	
	public List<String> getPriorityList() throws JSONException, Exception {
		List<String> localTestClasses = this.localClassService.getClassList();
		List<String> onlineTestClasses = new ArrayList<String>();
		
		try {
			onlineTestClasses.addAll(this.onlineClassService.getClassList());
		} catch (Exception e) {
			System.out.println("Could not conenct to server");
		}
		
		List<String> onlyLocal = new ArrayList<String>();
		List<String> finalList = new ArrayList<String>();
		
		for(String s : localTestClasses){
			if(!onlineTestClasses.contains(s)){
				onlyLocal.add(s);
			}
		}
		finalList.addAll(onlyLocal);
		for(String s : onlineTestClasses){
			if(localTestClasses.contains(s)){
				finalList.add(s);
			}
		}
		return finalList;
	}
}
