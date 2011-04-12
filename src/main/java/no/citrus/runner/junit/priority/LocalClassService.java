package no.citrus.runner.junit.priority;

import java.io.File;
import java.util.List;

import no.citrus.localprioritization.ClassListProvider;


public class LocalClassService implements ClassService {
	private final File testClassDirectory;
	
	public LocalClassService(File testClassDirectory){
		this.testClassDirectory = testClassDirectory;
	}

	@Override
	public List<String> getClassList() {
		return ClassListProvider.getClassList(this.testClassDirectory);
	}
}
