package no.citrus.runner.junit.priority;

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
