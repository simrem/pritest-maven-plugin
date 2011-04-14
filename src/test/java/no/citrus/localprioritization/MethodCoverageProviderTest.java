package no.citrus.localprioritization;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.ClassCover;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class MethodCoverageProviderTest {

	private MethodCoverageProvider methodCoverageProvider;
	private Map<String, ClassType> classTypes;
	private List<CompilationUnit> compilationUnits;
	
	@Before
	public void setup() throws ParseException, IOException{
		File sourceDir = new File("src/main/java");
		List<File> sourceFiles = ClassListProvider.getFileList(sourceDir, new String[] {".java"});
		compilationUnits = CompilationUnitProvider.getCompilationUnits(sourceFiles);
		ClassTypeProvider classTypeProvider = new ClassTypeProvider(compilationUnits);
		classTypes = classTypeProvider.getClassTypes();
	}
	
	@Test
	public void should_retrieve_map_of_methodcoverage_items(){
		this.methodCoverageProvider = new MethodCoverageProvider(classTypes, compilationUnits);
		Map<String, ClassCover> methodCoverageMap = methodCoverageProvider.getMethodCoverage();
		assertThat(methodCoverageMap.isEmpty(), equalTo(false));
	}
}
