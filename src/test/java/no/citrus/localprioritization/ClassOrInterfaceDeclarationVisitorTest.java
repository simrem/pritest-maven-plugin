package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class ClassOrInterfaceDeclarationVisitorTest {

    private List<ClassType> classes;

	@Before
	public void setup() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/FieldVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);

        ClassOrInterfaceDeclarationVisitor cidv = new ClassOrInterfaceDeclarationVisitor();

		cu.accept(cidv, null);

        classes = cidv.getClasses();
	}

	@Test
	public void shouldReturnClassName() {
        ClassType classType = classes.get(0);

        assertThat(classType.getName(), is(equalTo("FieldVisitor")));
	}

    @Test
    public void shouldReturnCorrectAmountOfClassesFromCompilationUnit() {
        assertThat(classes.size(), is(equalTo(3)));
    }
	
	@Test
    public void shouldReturnInnerClasses() {
        ClassType firstInnerClass = classes.get(1);
        ClassType secondInnerClass = classes.get(2);

        assertThat(firstInnerClass.getName(), is(equalTo("FieldVariableVisitor")));
        assertThat(secondInnerClass.getName(), is(equalTo("FieldTypeVisitor")));
    }

    @Test
    public void shouldIntegrateWithFieldVisitor() {
        ClassType ct1 = classes.get(0);
        ClassType ct2 = classes.get(1);
        ClassType ct3 = classes.get(2);

        assertThat(ct1.getFields().size(), is(equalTo(1)));
        assertThat(ct1.getFields(), hasItem(new ReferenceType("List", "fields")));
        
        assertThat(ct2.getFields().size(), is(equalTo(0)));
        
        assertThat(ct3.getFields().size(), is(equalTo(1)));
        assertThat(ct3.getFields(), hasItem(new ReferenceType("String", "name")));
    }

    @Test
    @Ignore
	public void shouldFindDeclaredMethodsInClass() {
        List<MethodDecl> methodDeclarations = classes.get(0).getMethodDeclarations();

		assertThat(methodDeclarations, hasItems(
				new MethodDecl("List", "getFields"),
				new MethodDecl("void", "visit")));
	}
}
