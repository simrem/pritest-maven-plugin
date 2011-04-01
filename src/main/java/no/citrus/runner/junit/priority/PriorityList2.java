package no.citrus.runner.junit.priority;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;

public class PriorityList2 {
	
	private final ClassService localClassService;
	private final ClassService onlineClassService;
	
	public PriorityList2(ClassService onlineClassService, ClassService localClassService){
		this.localClassService = localClassService;
		this.onlineClassService = onlineClassService;
	}
	
	public List<String> getPriorityList() throws JSONException, Exception {
		List<String> localTestClasses = this.localClassService.getClassList();
		List<String> onlineTestClasses = new ArrayList<String>();
		
		String technique = ((OnlineClassService) this.onlineClassService).getTechniqueURL();
		int techniqueNumber = Integer.parseInt(technique.substring(technique.length()-1, technique.length()));
		
		switch (techniqueNumber) {
			case 1: case 2: case 3:
				return onlineListStrategy(localTestClasses, onlineTestClasses);
		
			case 4:
				return technique4Strategy(localTestClasses);
				
			case 5:
				return technique5Strategy();
				
			case 6:
				// Sveinung sin teknikk.
		}
		return new ArrayList<String>();
	}
	
	private List<String> technique4Strategy(List<String> localTestClasses) {
		Technique4Ranker t4 = new Technique4Ranker(localTestClasses);
		return t4.getTechnique4PriorityList();
	}
	
	private List<String> technique5Strategy() {
		Technique5Ranker t5 = new Technique5Ranker();
		List<String> t5GitStatusList = t5.getTechnique5PriorityList();
		
		List<String> contactCitrusList = new ArrayList<String>();
		try {
			// Her vil vi ha tak i samme liste som dersom man bruker teknikk3.
			contactCitrusList = this.onlineClassService.getClassList();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String s : contactCitrusList) {
			if (!t5GitStatusList.contains(s)) {
				t5GitStatusList.add(s);
			}
		}
		
		return t5GitStatusList;
	}

	private List<String> onlineListStrategy(List<String> localTestClasses, List<String> onlineTestClasses) {
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
