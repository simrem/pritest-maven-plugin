package no.citrus.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class CompilationUnitProviderTest {
	private List<File> files;
	
	@Before
	public void setup(){
		files = ClassListProvider.getFileList(new File("src/main/java/"), new String[] {".java"});
	}
	
	@Test
	public void should_return_list_of_CompilationUnit_objects() throws ParseException, IOException{
		List<CompilationUnit> compilationUnits = CompilationUnitProvider.getCompilationUnits(files);
		assertThat(compilationUnits.isEmpty(), equalTo(false));
	}
}
