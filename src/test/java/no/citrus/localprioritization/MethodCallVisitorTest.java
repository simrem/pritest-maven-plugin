package no.citrus.localprioritization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.expr.MethodCallExpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MethodCallVisitorTest {
	@Test
	public void shouldFindMethodCallsInMethod() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/MethodCallVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		MethodCallVisitor mcv = new MethodCallVisitor();
		cu.accept(mcv, null);
		
		List<MethodCallExpr> methodCalls = mcv.getMethodCalls();
		
		ArrayList<String> methodCallStrings = new ArrayList<String>();
		for (MethodCallExpr mce : methodCalls) {
			methodCallStrings.add(mce.getName());
		}
		
		assertThat(methodCallStrings.contains("add"), equalTo(true));
	}
}
