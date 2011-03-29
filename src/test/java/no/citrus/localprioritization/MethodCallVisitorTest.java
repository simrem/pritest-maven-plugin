package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCallVisitorTest {
	
	@Test
	public void shouldFindMethodCallsInMethod() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/test/java/no/citrus/localprioritization/MethodCallVisitorTest.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		TypeDeclaration td = cu.getTypes().get(0);
		MethodDeclaration method = (MethodDeclaration) td.getMembers().get(0);
		
		MethodCallVisitor mcv = new MethodCallVisitor();
		method.accept(mcv, null);

		List<MethodCall> methodCalls = mcv.getMethodCalls();
		
		assertThat(methodCalls, hasItems(new MethodCall("mcv", "getMethodCalls"),
                new MethodCall("JavaParser", "parse")));
	}
}
