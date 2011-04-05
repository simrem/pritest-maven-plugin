package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodCover;
import no.citrus.localprioritization.model.MethodDecl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCoverageVisitorTest {

    private ClassCover methodDeclarationVisitorClass;

    @Before
    public void setup_class() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);

        Map<String, ClassType> classesInProject = new HashMap<String, ClassType>();

        ClassType callingClass = new ClassType("no.citrus.localprioritization.visitor", "MethodDeclarationVisitor");
        callingClass.getMethodDeclarations().add(new MethodDecl("List", "getMethodDeclarations", new ArrayList<String>()));
        List<String> params1 = new ArrayList<String>();
        params1.add("MethodDeclaration");
        params1.add("Object");
        callingClass.getMethodDeclarations().add(new MethodDecl("void", "visit", params1));

        ClassType firstClass = new ClassType("japa.parser.ast.body", "MethodDeclaration");
        firstClass.getMethodDeclarations().add(new MethodDecl("String", "getName", new ArrayList<String>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("Type", "getType", new ArrayList<String>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("List", "getParameters", new ArrayList<String>()));

        ClassType secondClass = new ClassType("japa.parser.ast.body", "Parameter");
        List<String> params2 = new ArrayList<String>();
        params2.add("GenericVisitor");
        params2.add("A");
        secondClass.getMethodDeclarations().add(new MethodDecl("R", "accept", params2));
        List<String> params3 = new ArrayList<String>();
        params3.add("VoidVisitor");
        params3.add("A");
        secondClass.getMethodDeclarations().add(new MethodDecl("void", "accept", params3));

        classesInProject.put("MethodDeclarationVisitor", callingClass);
        classesInProject.put("MethodDeclaration", firstClass);
        classesInProject.put("Parameter", secondClass);

        MethodCoverageVisitor mvc = new MethodCoverageVisitor(classesInProject);
        cu.getTypes().get(0).accept(mvc, null);
        Map<String, ClassCover> coveredClasses = mvc.getCoveredClasses();

        methodDeclarationVisitorClass = coveredClasses.get("MethodDeclarationVisitor");
    }

    @Test
    public void should_find_classes_in_compilation_unit() {
        assertThat(methodDeclarationVisitorClass.getClassName(), is(equalTo("MethodDeclarationVisitor")));
    }

    @Test
    @Ignore
    public void should_find_methods_declared_within_classes() {
        assertThat(methodDeclarationVisitorClass.getMethods().values(), hasItems(
                new MethodCover("List", "getMethodDeclarations")
        ));
    }
}
