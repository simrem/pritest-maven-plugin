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
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
