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
			System.out.println((double)(testcaseOrder + 1) / (double)numberOfTestcases);
			System.out.println((double)(numberOfFaultsInTestCases.get(testcaseOrder) / totalNumberOfFaults));
			result.add(0.0, 0.0);
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
		File output = new File(filename);
		
		APFDGraph graph = new APFDGraph(plotdata, output);
		graph.saveAsPNG();
		
	}
}
