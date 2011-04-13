package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import no.citrus.localprioritization.model.MethodDecl;
import no.citrus.localprioritization.model.ReferenceType;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MethodDeclarationVisitorTest {
	
	private MethodDecl methodDeclaration;

	@Before
	public void setup() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		MethodDeclarationVisitor mdv = new MethodDeclarationVisitor();
		BodyDeclaration bd = cu.getTypes().get(0).getMembers().get(0);
		methodDeclaration = bd.accept(mdv, null);
	}
	
	@Test
	public void should_find_declared_method() {
		List<ReferenceType> parameterTypes2 = new ArrayList<ReferenceType>();
		
		parameterTypes2.add(new ReferenceType("MethodDeclaration", "n"));
		parameterTypes2.add(new ReferenceType("Object", "arg1"));
		
		assertThat(methodDeclaration, is(equalTo(new MethodDecl("MethodDecl", "visit", parameterTypes2))));
	}
}
