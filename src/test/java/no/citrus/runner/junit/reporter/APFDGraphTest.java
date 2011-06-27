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

package no.citrus.runner.junit.reporter;

import java.io.File;
import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class APFDGraphTest {
	XYDataset dataset;
	File output;
	
	@Before
	public void setup(){
		XYSeries series = new XYSeries("APFD");
		series.add(0, 0);
		series.add(0.2, 0.2);
		series.add(0.3, 0.3);
		series.add(0.4, 0.4);
		series.add(0.5, 0.5);
		series.add(0.6, 0.6);
		series.add(0.7, 0.7);
		dataset = new XYSeriesCollection(series);
		output = new File("./testoutput");
	}
	
	@Test
	public void should_make_chart() throws IOException{
		APFDGraph graph = new APFDGraph(dataset, output);
		graph.saveAsPNG();
	}
	
	@After
	public void cleanup(){
		output.delete();
	}
}
