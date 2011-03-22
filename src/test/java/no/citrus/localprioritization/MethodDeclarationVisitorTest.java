package no.citrus.localprioritization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MethodDeclarationVisitorTest {
	
	@Test
	public void shouldFindDeclaredMethodInClass() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		MethodDeclarationVisitor mdv = new MethodDeclarationVisitor();
		mdv.visit(cu, null);
		
		ArrayList<String> methodNames = new ArrayList<String>();
		List<MethodDeclaration> methodDeclarations = mdv.getMethodDeclarations();
		for (MethodDeclaration md : methodDeclarations) {
			methodNames.add(md.getName());
		}
		
		assertThat(methodNames.contains("getMethodDeclarations"), equalTo(true));
		assertThat(methodNames.contains("visit"), equalTo(true));
	}
}
