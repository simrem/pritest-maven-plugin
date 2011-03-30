package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodDeclarationVisitorTest {
	
	private List<MethodDecl> methodDeclarations;

	@Before
	public void setup() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		MethodDeclarationVisitor mdv = new MethodDeclarationVisitor();
		TypeDeclaration td = cu.getTypes().get(0);
		td.accept(mdv, null);
		
		methodDeclarations = mdv.getMethodDeclarations();
	}
	
	@Test
	public void shouldFindDeclaredMethodInClass() {
		assertThat(methodDeclarations, hasItems(
				new MethodDecl("List", "getMethodDeclarations"),
				new MethodDecl("void", "visit")));
	}
}
