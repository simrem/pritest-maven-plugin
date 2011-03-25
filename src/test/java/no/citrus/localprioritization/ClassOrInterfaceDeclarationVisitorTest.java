package no.citrus.localprioritization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ClassOrInterfaceDeclarationVisitorTest {

	private ClassType classType;

	@Before
	public void makeClassType() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/ReferenceType.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		TypeDeclaration td = cu.getTypes().get(0);
		classType = td.accept(new ClassOrInterfaceDeclarationVisitor(), null);
	}
	
	@Test
	public void shouldReturnClassType() throws FileNotFoundException, ParseException {
		assertThat(classType, notNullValue());
	}
	
	@Test
	public void shouldReturnClassName() {
		assertThat(classType.getName(), equalTo("ReferenceType"));
	}
	
	@Test
	@Ignore
	public void shouldReturnFields() {
		List<ReferenceType> fields = classType.getFields();
		assertThat(fields.contains(new ReferenceType("String", "type")), equalTo(true));
	}
}
