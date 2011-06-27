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

package no.pritest.runner.junit.reporter;

import no.pritest.restapi.model.Measure;
import no.pritest.restapi.model.MeasureList;

public class APFD {
	public final MeasureList measureList;
	
	public APFD(MeasureList measureList){
		this.measureList = measureList;
	}
	
	public double calculateAPFD(){
		int testCaseNumber = 0;
    	int numberOfFailedTests = 0;
    	int tf = 0;
    	for(Measure testCase : measureList.getList()){
    		testCaseNumber++;
    		for(Measure testMethod : testCase.children){
    			if(testMethod.isFailed()){
    				numberOfFailedTests++;
    				tf += testCaseNumber;
    			}
    		}
    	}

    	double result = 0;
    	if(numberOfFailedTests > 0){
    		result = 1.0 - ((double)tf / ( (double)testCaseNumber * (double)numberOfFailedTests )) + (1.0 / (2.0 * testCaseNumber));
    	}
    	else{
    		result = Double.NaN;
    	}
    	return result;
	}
	
}
