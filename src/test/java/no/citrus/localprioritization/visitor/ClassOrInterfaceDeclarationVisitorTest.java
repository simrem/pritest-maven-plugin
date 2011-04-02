package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodDecl;
import no.citrus.localprioritization.model.ReferenceType;
import org.hamcrest.collection.IsCollectionContaining;
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
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/FieldVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);

        ClassOrInterfaceDeclarationVisitor cidv = new ClassOrInterfaceDeclarationVisitor();

		cu.accept(cidv, null);

        classes = cidv.getTypes();
	}

	@Test
	public void should_return_class_name() {
        ClassType classType = classes.get(0);

        assertThat(classType.getName(), is(equalTo("FieldVisitor")));
	}

    @Test
    public void should_return_correct_amount_of_classes_from_compilation_unit() {
        assertThat(classes.size(), is(equalTo(1)));
        assertThat(classes.get(0).getInnerClasses().size(), is(equalTo(2)));
    }
	
	@Test
    public void should_return_inner_classes() {
        ClassType firstInnerClass = classes.get(0).getInnerClasses().get(0);
        ClassType secondInnerClass = classes.get(0).getInnerClasses().get(1);

        assertThat(firstInnerClass.getName(), is(equalTo("FieldVariableVisitor")));
        assertThat(secondInnerClass.getName(), is(equalTo("FieldTypeVisitor")));
    }

    @Test
    public void should_integrate_with_FieldVisitor() {
        ClassType outerClass = classes.get(0);
        ClassType firstInnerClass = classes.get(0).getInnerClasses().get(0);
        ClassType secondInnerClass = classes.get(0).getInnerClasses().get(1);

        assertThat(outerClass.getFields().size(), is(equalTo(1)));
        assertThat(outerClass.getFields(), IsCollectionContaining.hasItem(new ReferenceType("List", "fields")));
        
        assertThat(firstInnerClass.getFields().size(), is(equalTo(0)));
        
        assertThat(secondInnerClass.getFields().size(), is(equalTo(1)));
        assertThat(secondInnerClass.getFields(), hasItem(new ReferenceType("String", "name")));
    }

    @Test
	public void should_integrate_with_MethodDeclarationVisitor() {
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
		assertThat(outerClass.getMethodDeclarations(), IsCollectionContaining.hasItems(
                new MethodDecl("List", "getFields", params1),
                new MethodDecl("void", "visit", params2)));
		
		assertThat(firstInnerClass.getMethodDeclarations().size(), is(equalTo(2)));
		
		assertThat(secondInnerClass.getMethodDeclarations().size(), is(equalTo(2)));
		assertThat(secondInnerClass.getMethodDeclarations(), hasItems(
				new MethodDecl("void", "visit", params3),
				new MethodDecl("String", "getName", params4)));
	}
}
