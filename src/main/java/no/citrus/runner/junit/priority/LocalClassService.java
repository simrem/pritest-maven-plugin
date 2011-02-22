package no.citrus.runner.junit.priority;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LocalClassService implements ClassService {

	private static final String FILE_SUFFIX = ".class";
	private final File testClassDirectory;
	
	public LocalClassService(File testClassDirectory){
		this.testClassDirectory = testClassDirectory;
	}

	@Override
	public List<String> getClassList() {
		List<File> fileList = getFileList(testClassDirectory);
		List<String> testClassNames = new ArrayList<String>();
		String testDir = testClassDirectory.getAbsolutePath() + "/";
		for(File f : fileList){
			String filePath = f.getAbsolutePath();
			filePath = filePath.replace(testDir, "");
			filePath = filePath.replaceAll("/", ".");
			filePath = filePath.substring(0, filePath.length() - FILE_SUFFIX.length());
			testClassNames.add(filePath);
		}
		return testClassNames;
	}
	
	private static List<File> getFileList(File directory){
		List<File> list = new ArrayList<File>();
		if(directory.isDirectory())
		{
			File f;
			File[] localList = directory.listFiles();
			for(int i = 0; i < localList.length; i++){
				f = localList[i];
				if(f.isDirectory()){
					list.addAll(getFileList(f));
				}
				else{
					if(f.getName().endsWith(FILE_SUFFIX))
					list.add(f);
				}
			}
		}
		return list;
	}
}
