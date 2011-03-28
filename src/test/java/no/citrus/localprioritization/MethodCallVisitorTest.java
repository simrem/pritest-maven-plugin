package no.citrus.localprioritization;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

public class MethodCallVisitorTest {
	
	@Test
	public void shouldFindMethodCallsInMethod() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/MethodCall.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		TypeDeclaration td = cu.getTypes().get(0);
		MethodDeclaration method = (MethodDeclaration) td.getMembers().get(3);
		
		MethodCallVisitor mcv = new MethodCallVisitor();
		method.accept(mcv, null);
		
		List<MethodCall> methodCalls = mcv.getMethodCalls();
		
		assertThat(methodCalls, hasItem(new MethodCall("hashCode")));
	}
}
