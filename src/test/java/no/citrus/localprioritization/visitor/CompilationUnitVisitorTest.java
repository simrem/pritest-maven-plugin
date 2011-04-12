package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.visitor.CompilationUnitVisitor;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class CompilationUnitVisitorTest {

	private static CompilationUnitVisitor cuv;

	@BeforeClass
	public static void setup_class() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/CompilationUnitVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		cuv = new CompilationUnitVisitor();
		cu.accept(cuv, null);
	}
	
	@Test
	public void should_integrate_with_ClassOrInterfaceDeclarationVisitors_class_name() {
		List<ClassType> types = cuv.getTypes();
		
		assertThat(types.get(0).getName(), is(equalTo("CompilationUnitVisitor")));
	}
	
	@Test
	public void should_integrate_with_ClassOrInterfaceDeclarationVisitors_package_name() {
		List<ClassType> types = cuv.getTypes();
		
		assertThat(types.get(0).getPackageName(), is(equalTo("no.citrus.localprioritization.visitor")));
		assertThat(types.get(0).getInnerClasses().get(0).getPackageName(),
				is(equalTo("no.citrus.localprioritization.visitor.CompilationUnitVisitor")));
	}
	
	@Test
	public void should_retrieve_import_statements() {
		List<String> imports = cuv.getImportStatements();
		
		assertThat(imports, hasItems(
				"no.citrus.localprioritization.model.ClassType",
				"japa.parser.ast.visitor.VoidVisitorAdapter"));
	}
	
	@Test
	public void should_get_package_name() {
		String packageName = cuv.getPackageName();
		
		assertThat(packageName, is(equalTo("no.citrus.localprioritization.visitor")));
	}
	
	@Test
	public void should_provide_class_types_in_a_map() {
		Map<String, ClassType> types = cuv.getTypesAsMapItems();
		
		ClassType type = types.get("CompilationUnitVisitor");
		
		assertThat(type.getName(), is(equalTo("CompilationUnitVisitor")));
		assertThat(type.getMethodDeclarations().get(0).getMethodName(), is(equalTo("visit")));
	}
}
