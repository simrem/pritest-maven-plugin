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
