package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.visitor.CompilationUnitVisitor;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class CompilationUnitVisitorTest {

	private static CompilationUnitVisitor cuv;

	@BeforeClass
	public static void setup_class() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/FieldVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		cuv = new CompilationUnitVisitor();
		cu.accept(cuv, null);
	}
	
	@Test
	public void should_integrate_with_ClassOrInterfaceDeclarationVisitor() {
		List<ClassType> types = cuv.getTypes();
		
		assertThat(types.get(0).getName(), is(equalTo("FieldVisitor")));
	}
	
	@Test
	public void should_retrieve_import_statements() {
		List<String> imports = cuv.getImportStatements();
		
		assertThat(imports, hasItems(
				"japa.parser.ast.body.FieldDeclaration",
				"japa.parser.ast.body.VariableDeclarator"));
	}
}
