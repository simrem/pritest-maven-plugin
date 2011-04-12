package no.citrus.localprioritization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassListProvider {
	private static final String CLASS_SUFFIX = ".class";
	
	public static List<String> getClassList(File dir) {
		List<String> result = new ArrayList<String>();	
		List<File> fileList = getFileList(dir, new String[] {CLASS_SUFFIX});
		String testDir = dir.getAbsolutePath() + "/";
		for(File f : fileList){
			String filePath = f.getAbsolutePath();
			filePath = filePath.replace(testDir, "");
			filePath = filePath.replaceAll("/", ".");
			filePath = filePath.substring(0, filePath.length() - CLASS_SUFFIX.length());
			result.add(filePath);
		}
		return result;
	}
	
	public static List<File> getFileList(File directory, String[] validSuffixes){
		List<File> list = new ArrayList<File>();
		if(directory.isDirectory())
		{
			for(File f : directory.listFiles()){
				if(f.isDirectory()){
					list.addAll(getFileList(f, validSuffixes));
				}
				else{
					for(String suffix : validSuffixes){
						if(f.getName().endsWith(suffix))
						{
							list.add(f);
							break;
						}
					}
					
				}
			}
		}
		return list;
	}
}
