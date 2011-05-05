package no.citrus.runner.junit.reporter;

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;
import no.citrus.runner.junit.reporter.APFD;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class APFDTest {

    private MeasureList measureList;

    @Before
    public void prepare_MeasureList(){
        List<Measure> list = new ArrayList<Measure>();
        for(int i = 0; i < 2; i++){
            Measure testMeasure;
            testMeasure = new Measure();
            testMeasure.setName(String.format("Class%d", i));
            testMeasure.setDate(new Date(System.currentTimeMillis()));
            testMeasure.setChildren(new ArrayList<Measure>());
            if(i == 1){
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
    public void should_calculate_APFD_test(){
    	
    	APFD apfd = new APFD(measureList);
    	double result = apfd.calculateAPFD(); 
    	
    	assertThat(result, equalTo(0.25));
    }
    
    @Test
    public void should_support_all_failure_scenarios(){
    	List<Measure> list = new ArrayList<Measure>();
        for(int i = 0; i < 2; i++){
            Measure testMeasure;
            testMeasure = new Measure();
            testMeasure.setName(String.format("Class%d", i));
            testMeasure.setDate(new Date(System.currentTimeMillis()));
            testMeasure.setChildren(new ArrayList<Measure>());
            Measure failedTestMethod = new Measure();
            failedTestMethod.failed = true;
            failedTestMethod.setDate(new Date(System.currentTimeMillis()));
            failedTestMethod.setName(String.format("MethodThatBelongsToClass%d", i));
            	
            testMeasure.getChildren().add(failedTestMethod);
            
            list.add(testMeasure);
        }
        
        APFD apfd = new APFD(new MeasureList(list));
        double result = apfd.calculateAPFD();
        assertThat(result, equalTo(0.5));
    }
    
    
}
