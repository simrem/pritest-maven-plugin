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

package no.pritest.reporter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

import java.io.File;
import java.io.IOException;

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
