package no.citrus.runner.junit.reporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    	double result = 0;
    	if(numberOfFailedTests > 0){
    		result = 1.0 - ((double)tf / ( (double)testCaseNumber * (double)numberOfFailedTests )) + (1.0 / (2.0 * testCaseNumber));
    	}
    	else{
    		result = Double.NaN;
    	}
    	return result;
	}
	
	public void outputToFile(String directory, String filename) throws IOException{
		File dir = new File(directory);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(directory + filename);
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(String.valueOf(calculateAPFD()));
		bw.close();
	}
}
