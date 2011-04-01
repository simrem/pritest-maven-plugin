package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

        classes = cidv.getTypes();
	}

	@Test
	public void shouldReturnClassName() {
        ClassType classType = classes.get(0);

        assertThat(classType.getName(), is(equalTo("FieldVisitor")));
	}

    @Test
    public void shouldReturnCorrectAmountOfClassesFromCompilationUnit() {
        assertThat(classes.size(), is(equalTo(1)));
        assertThat(classes.get(0).getInnerClasses().size(), is(equalTo(2)));
    }
	
	@Test
    public void shouldReturnInnerClasses() {
        ClassType firstInnerClass = classes.get(0).getInnerClasses().get(0);
        ClassType secondInnerClass = classes.get(0).getInnerClasses().get(1);

        assertThat(firstInnerClass.getName(), is(equalTo("FieldVariableVisitor")));
        assertThat(secondInnerClass.getName(), is(equalTo("FieldTypeVisitor")));
    }

    @Test
    public void shouldIntegrateWithFieldVisitor() {
        ClassType outerClass = classes.get(0);
        ClassType firstInnerClass = classes.get(0).getInnerClasses().get(0);
        ClassType secondInnerClass = classes.get(0).getInnerClasses().get(1);

        assertThat(outerClass.getFields().size(), is(equalTo(1)));
        assertThat(outerClass.getFields(), hasItem(new ReferenceType("List", "fields")));
        
        assertThat(firstInnerClass.getFields().size(), is(equalTo(0)));
        
        assertThat(secondInnerClass.getFields().size(), is(equalTo(1)));
        assertThat(secondInnerClass.getFields(), hasItem(new ReferenceType("String", "name")));
    }

    @Test
	public void shouldIntegrateWithMethodDeclarationVisitor() {
        ClassType outerClass = classes.get(0);
        ClassType firstInnerClass = classes.get(0).getInnerClasses().get(0);
        ClassType secondInnerClass = classes.get(0).getInnerClasses().get(1);

        List<String> params1 = new ArrayList<String>();
        List<String> params2 = new ArrayList<String>();
        List<String> params3 = new ArrayList<String>();
        List<String> params4 = new ArrayList<String>();

        params2.add("FieldDeclaration");
        params2.add("Object");
        params3.add("ClassOrInterfaceType");
        params3.add("Object");
        
        assertThat(outerClass.getMethodDeclarations().size(), is(equalTo(2)));
		assertThat(outerClass.getMethodDeclarations(), hasItems(
				new MethodDecl("List", "getFields", params1),
				new MethodDecl("void", "visit", params2)));
		
		assertThat(firstInnerClass.getMethodDeclarations().size(), is(equalTo(2)));
		
		assertThat(secondInnerClass.getMethodDeclarations().size(), is(equalTo(2)));
		assertThat(secondInnerClass.getMethodDeclarations(), hasItems(
				new MethodDecl("void", "visit", params3),
				new MethodDecl("String", "getName", params4)));
	}
}
