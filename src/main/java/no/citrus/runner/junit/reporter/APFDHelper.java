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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import no.citrus.restapi.model.Measure;
import no.citrus.restapi.model.MeasureList;

public class APFDHelper {

	public static List<Measure> sortMeasureListBySource(List<String> priorityList, List<Measure> measureList) {
		List<Measure> localMeasureList = new ArrayList<Measure>();
		for(String priorityItem : priorityList){
			for(Measure measure : measureList){
				if(measure.getSource().equals(priorityItem)){
					localMeasureList.add(measure);
					break;
				}
			}
		}
		for(Measure measure : measureList) {
			if(!localMeasureList.contains(measure)){
				localMeasureList.add(measure);
			}
		}
		return localMeasureList;
	}
	
	public static void outputAPFDToFile(APFD apfd, int techniqueNumber) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String directory = "apfd/" + techniqueNumber + "/";
		String filename = sdf.format(new Date()) + ".txt";
		
		File dir = new File(directory);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File file = new File(directory + filename);
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(String.valueOf(apfd.calculateAPFD()));
		bw.close();
	}

	public static XYSeries getXYSeries(Integer localTechniqueNumber, List<Measure> localMeasureList) {
		XYSeries result = new XYSeries(localTechniqueNumber);
		result.add(0.0, 0.0);
		int totalNumberOfFaults = 0;
		int numberOfTestcases = localMeasureList.size();
		List<Integer> numberOfFaultsInTestCases = new ArrayList<Integer>();
		for(Measure measure : localMeasureList) {
			totalNumberOfFaults += measure.numOfFails;
			numberOfFaultsInTestCases.add(measure.numOfFails);
		}
		double covered = 0.0;
		for(int testcaseOrder = 0; testcaseOrder < numberOfTestcases; testcaseOrder++) {
			covered += (double)numberOfFaultsInTestCases.get(testcaseOrder) / (double)totalNumberOfFaults;
			result.add(
					(double)(testcaseOrder + 1) / (double)numberOfTestcases, 
					covered);
		}
		return result;
	}

	public static void outputAPFDGraphToFile(XYSeriesCollection plotdata) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String directory = "apfd/graphs/";
		String filename = sdf.format(new Date()) + ".png";
		
		File dir = new File(directory);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File output = new File(directory + filename);
		
		APFDGraph graph = new APFDGraph(plotdata, output);
		graph.saveAsPNG();
		
	}

	public static void outputSingleAPFDGraphToFile(XYSeries localPlotdata,
			Integer techniqueNumber) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String directory = "apfd/" + techniqueNumber + "/";
		String filename = sdf.format(new Date()) + ".png";
//		
//		File dir = new File(directory);
//		if(!dir.exists()){
//			dir.mkdirs();
//		}
		File file = new File(directory + filename);
		APFDGraph graph = new APFDGraph(new XYSeriesCollection(localPlotdata), file);
		graph.saveAsPNG();
		
	}
}
