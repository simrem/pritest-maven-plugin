package no.citrus.runner;

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;
import no.citrus.runner.junit.reporter.APFD;
import no.citrus.runner.junit.reporter.Reporter;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientHandlerException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class APFDTest {

    private MeasureList measureList;

    @Before
    public void prepareMeasureList(){
        List<Measure> list = new ArrayList<Measure>();
        for(int i = 0; i < 100; i++){
            Measure testMeasure;
            testMeasure = new Measure();
            testMeasure.setName(String.format("Class%d", i));
            testMeasure.setDate(new Date(System.currentTimeMillis()));
            testMeasure.setChildren(new ArrayList<Measure>());
            if(i%5 == 0){
            	Measure failedTestMethod = new Measure();
            	failedTestMethod.failed = true;
            	failedTestMethod.setDate(new Date(System.currentTimeMillis()));
            	failedTestMethod.setName(String.format("MethodThatBelongsToClass%d", i));
            	
            	testMeasure.getChildren().add(failedTestMethod);
            }
            
            list.add(testMeasure);
        }
        measureList = new MeasureList(list);
    }
    
    @Test
    public void shouldCalculateAPFDTest(){
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
    	double shouldBe = 1 - (tf / ( testCaseNumber * numberOfFailedTests )) + (1 / (2 * testCaseNumber));
    	
    	APFD apfd = new APFD(measureList);
    	double result = apfd.calculateAPFD(); 
    	
    	assertThat(result, equalTo(shouldBe));
    }
    
    
}
