package no.citrus.localprioritization;

import japa.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassType;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class ClassTypeProviderTest {
	
	private ClassTypeProvider classTypeProvider;
	private List<File> projectFiles;
	
	@Before
	public void setup() throws ParseException, IOException{
		File projectDirectory = new File("src/main/java/");
		this.projectFiles = ClassListProvider.getFileList(projectDirectory, new String[] {".java"});
		classTypeProvider = new ClassTypeProvider(this.projectFiles);
	}
	
	@Test
	public void projectFiles_should_have_at_least_one_file(){
		assertThat(this.projectFiles.isEmpty(), equalTo(false));
	}
	
	@Test
	public void ensure_that_map_of_classtypes_has_items(){
		Map<String, ClassType> classTypes = this.classTypeProvider.getClassTypes();
		assertThat(classTypes.isEmpty(), equalTo(false));
	}
}
