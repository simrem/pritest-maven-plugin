package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import no.citrus.localprioritization.model.RawMethodCall;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCallVisitorTest {
	
	@Test
	public void should_find_method_calls_in_method() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodCallVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		TypeDeclaration td = cu.getTypes().get(0);
		MethodDeclaration method = (MethodDeclaration) td.getMembers().get(2);
		
		MethodCallVisitor mcv = new MethodCallVisitor();
		method.accept(mcv, null);

		List<RawMethodCall> methodCalls = mcv.getRawMethodCalls();
		
		assertThat(methodCalls, hasItems(new RawMethodCall("n", "getName"),
                new RawMethodCall("n", "getScope"),
                new RawMethodCall("methodCalls", "add")));
	}
}
