package no.citrus.runner.junit.reporter;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

public class APFDGraph {

	private final XYDataset dataset;
	private final File output;
	private JFreeChart chart; 

	public APFDGraph(XYDataset dataset, File output) {
		this.dataset = dataset;
		this.output = output;
		makeGraph();
	}

	private void makeGraph() {
		chart = ChartFactory.createXYLineChart(
				"APFD", 
				"Test suite fraction", 
				"Precent Detected Faults", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);
		
	}

	public void saveAsPNG() throws IOException {
		ChartUtilities.saveChartAsPNG(output, chart, 640, 480, null, true, 0);
		
	}

}
