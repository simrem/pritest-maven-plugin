package no.citrus.localprioritization.visitor;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import no.citrus.localprioritization.model.ReferenceType;

import org.junit.Test;

public class VariableDeclarationVisitorTest {
	
	@Test
	public void should_find_variable_declarations_in_method_body() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/model/ReferenceType.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		VariableDeclarationVisitor vdv = new VariableDeclarationVisitor();
		cu.getTypes().get(0).getMembers().get(6).accept(vdv, null);
		Map<String, ReferenceType> variables = vdv.getVariables();
		
		assertThat(variables.values(), hasItems(new ReferenceType("ReferenceType", "other")));
	}
}
