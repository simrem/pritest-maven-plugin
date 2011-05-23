package no.citrus.runner.junit.reporter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.citrus.restapi.model.Measure;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class APFDHelperTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_calculate_apfdgraph_plots_from_measurelist(){
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
            	testMeasure.setNumOfFails(1);
            }
            
            list.add(testMeasure);
        }
        XYSeries result = new XYSeries(0);
        result.add(0.0, 0.0);
        result.add(0.5, 0.0);
        result.add(1.0, 1.0);
        
        
        XYSeries plotdata = APFDHelper.getXYSeries(0, list);
        
        assertTrue(((List<XYDataItem>)result.getItems()).containsAll(plotdata.getItems()));
	}
}