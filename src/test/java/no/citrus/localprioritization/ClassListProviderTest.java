package no.citrus.localprioritization;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class ClassListProviderTest {
	File directory;
	File[] expectedFileListResult;
	String[] expectedClassListResult = new String[] {
			"Models.ModelA"
	};
	
	@Before
	public void setupDirectoryMock(){
		File readme = mock(File.class);
		when(readme.getAbsolutePath()).thenReturn("/project/Readme.txt");
		when(readme.getName()).thenReturn("Readme.txt");
		when(readme.isDirectory()).thenReturn(false);
		
		File classA = mock(File.class);
		when(classA.getAbsolutePath()).thenReturn("/project/ClassA.java");
		when(classA.getName()).thenReturn("ClassA.java");
		when(classA.isDirectory()).thenReturn(false);
		
		File classB = mock(File.class);
		when(classB.getAbsolutePath()).thenReturn("/project/ClassB.java");
		when(classB.getName()).thenReturn("ClassB.java");
		when(classB.isDirectory()).thenReturn(false);
		
		File modelA = mock(File.class);
		when(modelA.getAbsolutePath()).thenReturn("/project/Models/ModelA.java");
		when(modelA.getName()).thenReturn("ModelA.java");
		when(modelA.isDirectory()).thenReturn(false);
		
		File modelAClass = mock(File.class);
		when(modelAClass.getAbsolutePath()).thenReturn("/project/Models/ModelA.class");
		when(modelAClass.getName()).thenReturn("ModelA.class");
		when(modelAClass.isDirectory()).thenReturn(false);
		
		File modelsDir = mock(File.class);
		when(modelsDir.getAbsolutePath()).thenReturn("/project/Models");
		when(modelsDir.isDirectory()).thenReturn(true);
		when(modelsDir.listFiles()).thenReturn(new File[] {modelA, modelAClass});
		
		File dir = mock(File.class);
		when(dir.getAbsolutePath()).thenReturn("/project");
		when(dir.isDirectory()).thenReturn(true);
		when(dir.listFiles()).thenReturn(new File[] {readme, classA, classB, modelsDir});
		
		this.directory = dir;
		
		this.expectedFileListResult = new File[] {classA, classB, modelA};
	}
	
	
	@Test
	public void shouldReturnClassList(){
		List<String> classList = ClassListProvider.getClassList(directory);
		assertThat(classList.toArray(new String[0]), equalTo(expectedClassListResult));
	}
	
	@Test
	public void shouldReturnFileList(){
		List<File> fileList = ClassListProvider.getFileList(directory, new String[] {".java"});
		assertThat(fileList.toArray(new File[0]), equalTo(expectedFileListResult));
	}
}
