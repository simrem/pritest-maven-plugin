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

import no.pritest.localprioritization.AdditionalMethodCoverage;
import no.pritest.localprioritization.TotalMethodCoverage;
import org.codehaus.jettison.json.JSONException;
import org.eclipse.jgit.errors.NoWorkTreeException;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriorityList2 {
	
	private final ClassService localClassService;
	private final ClassService onlineClassService;
	private final File baseDir;
	private final int techniqueNumber;
	private final String testSourceDirectory;
	private final String sourceDirectory;
	
	
	public PriorityList2(ClassService onlineClassService, ClassService localClassService, File basedir, int techniqueNumber, String testSourceDirectory, String sourceDirectory){
		this.localClassService = localClassService;
		this.onlineClassService = onlineClassService;
		this.baseDir = basedir;
		this.techniqueNumber = techniqueNumber;
		this.testSourceDirectory = testSourceDirectory;
		this.sourceDirectory = sourceDirectory;
		
	}
	
	public List<String> getPriorityList() throws JSONException, NoWorkTreeException, IOException, Exception {
		List<String> localTestClasses = this.localClassService.getClassList();
		List<String> onlineTestClasses = new ArrayList<String>();
		
		switch (techniqueNumber) {
			case 1: case 2: case 3:
				return onlineListStrategy(localTestClasses, onlineTestClasses);
			case 4:
				return technique4Strategy(localTestClasses);
			case 5:
				return technique5Strategy();
			case 6:
				TotalMethodCoverage tmc = new TotalMethodCoverage(sourceDirectory, testSourceDirectory);
				return tmc.getTestCases();
			case 7:
				AdditionalMethodCoverage amc = new AdditionalMethodCoverage(sourceDirectory, testSourceDirectory);
				return amc.getTestCases();
			case 8:
				return localTestClasses;
			case 9:
				return randomLocalTestClasses(localTestClasses);
		}
		return new ArrayList<String>();
	}

	private List<String> randomLocalTestClasses(List<String> localTestClasses) {
		Collections.shuffle(localTestClasses);
		return localTestClasses;
	}
	
	

	private List<String> technique4Strategy(List<String> localTestClasses) throws NoWorkTreeException, IOException {
		GitStatusProvider gsp = new GitStatusProvider(baseDir, sourceDirectory, testSourceDirectory);
		List<String> gitStatusPriorityList = gsp.getGitStatusPriorityList();
		for(String localTestClass : localTestClasses) {
			if(!gitStatusPriorityList.contains(localTestClass)) {
				gitStatusPriorityList.add(localTestClass);
			}
		}
		return gitStatusPriorityList;
	}
	
	private List<String> technique5Strategy() throws NoWorkTreeException, IOException {
		GitStatusProvider gsp = new GitStatusProvider(baseDir, sourceDirectory, testSourceDirectory);
		List<String> gitStatusPriorityList = gsp.getGitStatusPriorityList();
		
		List<String> contactCitrusList = new ArrayList<String>();
		try {
			// Her vil vi ha tak i samme liste som dersom man bruker teknikk1.
			contactCitrusList = this.onlineClassService.getClassList();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String s : contactCitrusList) {
			if (!gitStatusPriorityList.contains(s)) {
				gitStatusPriorityList.add(s);
			}
		}
		
		return gitStatusPriorityList;
	}

	private List<String> onlineListStrategy(List<String> localTestClasses, List<String> onlineTestClasses) {
		try {
			onlineTestClasses.addAll(this.onlineClassService.getClassList());
		} catch (Exception e) {
			System.out.println("Could not connect to server");
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
