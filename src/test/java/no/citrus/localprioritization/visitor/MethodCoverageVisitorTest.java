package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.*;
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
    public void setup() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);

        Map<String, ClassType> classesInProject = new HashMap<String, ClassType>();

        ClassType callingClass = new ClassType("no.citrus.localprioritization.visitor", "MethodDeclarationVisitor");
        callingClass.getMethodDeclarations().add(new MethodDecl("List", "getMethodDeclarations", new ArrayList<ReferenceType>()));
        List<ReferenceType> params1 = new ArrayList<ReferenceType>();
        params1.add(new ReferenceType("MethodDeclaration", "n"));
        params1.add(new ReferenceType("Object", "arg1"));
        callingClass.getMethodDeclarations().add(new MethodDecl("void", "visit", params1));

        ClassType firstClass = new ClassType("japa.parser.ast.body", "MethodDeclaration");
        firstClass.getMethodDeclarations().add(new MethodDecl("String", "getName", new ArrayList<ReferenceType>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("Type", "getType", new ArrayList<ReferenceType>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("List", "getParameters", new ArrayList<ReferenceType>()));

        ClassType secondClass = new ClassType("japa.parser.ast.body", "Parameter");
        List<ReferenceType> params2 = new ArrayList<ReferenceType>();
        params2.add(new ReferenceType("GenericVisitor", "v"));
        params2.add(new ReferenceType("A", "arg"));
        secondClass.getMethodDeclarations().add(new MethodDecl("R", "accept", params2));
        List<ReferenceType> params3 = new ArrayList<ReferenceType>();
        params3.add(new ReferenceType("VoidVisitor", "v"));
        params3.add(new ReferenceType("A", "arg"));
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
        assertThat(methodDeclarationVisitorClass.getName(), is(equalTo("MethodDeclarationVisitor")));
    }

    @Test
    @Ignore
    public void should_find_methods_declared_within_classes() {
    	List<ReferenceType> params1 = new ArrayList<ReferenceType>();
    	
    	params1.add(new ReferenceType("MethodDeclaration", "n"));
    	params1.add(new ReferenceType("Object", "arg1"));
    	
        List<ProcessedMethodCall> methodCalls = new ArrayList<ProcessedMethodCall>();
        methodCalls.add(new ProcessedMethodCall("MethodDeclaration", "getType", new ArrayList<String>()));
        methodCalls.add(new ProcessedMethodCall("MethodDeclaration", "getParameters", new ArrayList<String>()));
        //methodCalls.add(new ProcessedMethodCall("Parameter", "accept"));

        assertThat(methodDeclarationVisitorClass.getMethods().values(), hasItems(
                new MethodCover("MethodDeclarationVisitor", "void", "visit", params1, methodCalls),
                new MethodCover("MethodDeclarationVisitor", "List", "getMethodDeclarations", new ArrayList<ReferenceType>(),
                		new ArrayList<ProcessedMethodCall>())
        ));
    }
}
