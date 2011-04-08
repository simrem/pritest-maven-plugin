package no.citrus.runner.junit.reporter;

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;

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
    	double result = 1.0 - (tf / ( testCaseNumber * numberOfFailedTests )) + (1.0 / (2.0 * testCaseNumber));
    	return result;
	}
}
