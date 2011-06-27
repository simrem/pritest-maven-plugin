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

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;
import no.pritest.runner.junit.reporter.APFD;

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
