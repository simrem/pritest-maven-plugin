package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import no.citrus.localprioritization.model.MethodDecl;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MethodDeclarationVisitorTest {
	
	private List<MethodDecl> methodDeclarations;

	@Before
	public void setup() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		MethodDeclarationVisitor mdv = new MethodDeclarationVisitor();
		TypeDeclaration td = cu.getTypes().get(0);
		td.accept(mdv, null);
		
		methodDeclarations = mdv.getMethodDeclarations();
	}
	
	@Test
	public void should_find_declared_method_in_class() {
		List<String> parameterTypes1 = new ArrayList<String>();
		List<String> parameterTypes2 = new ArrayList<String>();
		
		parameterTypes2.add("MethodDeclaration");
		parameterTypes2.add("Object");
		
		assertThat(methodDeclarations, hasItems(
				new MethodDecl("List", "getMethodDeclarations", parameterTypes1),
				new MethodDecl("void", "visit", parameterTypes2)));
		
		assertThat(methodDeclarations.get(0).getParameters().size(), is(equalTo(0)));
		assertThat(methodDeclarations.get(1).getParameters().size(), is(equalTo(2)));
	}
}
