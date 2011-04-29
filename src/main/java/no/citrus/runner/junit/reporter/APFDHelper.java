package no.citrus.runner.junit.reporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
