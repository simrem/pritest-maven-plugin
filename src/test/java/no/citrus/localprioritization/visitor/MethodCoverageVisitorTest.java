package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.*;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCoverageVisitorTest {

    private ClassCover methodDeclarationVisitorClass;

    @Before
    public void setup_MethodDeclarationVisitor_test_object() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);

        Map<String, ClassType> classesInProject = new HashMap<String, ClassType>();

        ClassType callingClass = new ClassType("no.citrus.localprioritization.visitor", "MethodDeclarationVisitor", null);
        callingClass.getMethodDeclarations().add(new MethodDecl("List", "getMethodDeclarations", new ArrayList<ReferenceType>()));
        List<ReferenceType> params1 = new ArrayList<ReferenceType>();
        params1.add(new ReferenceType("MethodDeclaration", "n"));
        params1.add(new ReferenceType("Object", "arg1"));
        callingClass.getMethodDeclarations().add(new MethodDecl("void", "visit", params1));

        ClassType firstClass = new ClassType("japa.parser.ast.body", "MethodDeclaration", null);
        firstClass.getMethodDeclarations().add(new MethodDecl("String", "getName", new ArrayList<ReferenceType>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("Type", "getType", new ArrayList<ReferenceType>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("List", "getParameters", new ArrayList<ReferenceType>()));

        ClassType secondClass = new ClassType("japa.parser.ast.body", "Parameter", null);
        List<ReferenceType> params2 = new ArrayList<ReferenceType>();
        params2.add(new ReferenceType("GenericVisitor", "v"));
        params2.add(new ReferenceType("A", "arg"));
        secondClass.getMethodDeclarations().add(new MethodDecl("R", "accept", params2));
        List<ReferenceType> params3 = new ArrayList<ReferenceType>();
        params3.add(new ReferenceType("VoidVisitor", "v"));
        params3.add(new ReferenceType("A", "arg"));
        secondClass.getMethodDeclarations().add(new MethodDecl("void", "accept", params3));

        ClassType thirdClass = new ClassType("no.citrus.localprioritization.visitor", "ReturnTypeVisitor", null);
        thirdClass.getMethodDeclarations().add(new MethodDecl("String", "getTypeName", new ArrayList<ReferenceType>()));

        classesInProject.put("MethodDeclarationVisitor", callingClass);
        classesInProject.put("MethodDeclaration", firstClass);
        classesInProject.put("ReturnTypeVisitor", thirdClass);

        MethodCoverageVisitor mcv = new MethodCoverageVisitor(classesInProject);
        cu.getTypes().get(0).accept(mcv, null);
        Map<String, ClassCover> coveredClasses = mcv.getCoveredClasses();

        methodDeclarationVisitorClass = coveredClasses.get("MethodDeclarationVisitor");
    }

    @Test
    public void should_find_classes_in_compilation_unit() {
        String className = methodDeclarationVisitorClass.getName();
        
        assertThat(className, is(equalTo("MethodDeclarationVisitor")));
    }

    @Test
    public void should_find_methods_declared_within_classes() {
    	List<ReferenceType> params1 = new ArrayList<ReferenceType>();
    	
    	params1.add(new ReferenceType("MethodDeclaration", "n"));
    	params1.add(new ReferenceType("Object", "arg1"));
    	
        List<ProcessedMethodCall> methodCalls = new ArrayList<ProcessedMethodCall>();
        methodCalls.add(new ProcessedMethodCall("ReturnTypeVisitor", "getTypeName", new ArrayList<String>()));

        assertThat(methodDeclarationVisitorClass.getMethods().values(), hasItems(
                new MethodCover("MethodDeclarationVisitor", "MethodDecl", "visit", params1, methodCalls)
        ));
    }

    @Test
    public void should_not_include_methods_not_belonging_to_the_analyzed_project() {
        List<ReferenceType> parameters = new ArrayList<ReferenceType>();
        parameters.add(new ReferenceType("E", "e"));

        assertThat(methodDeclarationVisitorClass.getMethods().values(), not(hasItems(
                new MethodCover("List", "boolean", "add", parameters, new ArrayList<ProcessedMethodCall>())
        )));
    }
    
    @Test
    public void should_include_methods_called_by_field_variables() throws ParseException, FileNotFoundException {
    	FileInputStream fis = new FileInputStream("src/test/java/no/citrus/localprioritization/visitor/MethodCoverageVisitorTest.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		Map<String, ClassType> classesInProject = new HashMap<String, ClassType>();
		
		ClassType classCoverClass = new ClassType("no.citrus.localprioritization.model", "ClassCover", null);
		classCoverClass.getMethodDeclarations().add(new MethodDecl("String", "getName", new ArrayList<ReferenceType>()));
		classesInProject.put("ClassCover", classCoverClass);

        ClassType theTestClass = new ClassType("no.citrus.localprioritization.visitor", "MethodCoverageVisitorTest", null);
        theTestClass.getMethodDeclarations().add(new MethodDecl("void", "should_find_classes_in_compilation_unit", new ArrayList<ReferenceType>()));
        theTestClass.getFields().put("methodDeclarationVisitorClass", new ReferenceType("ClassCover", "methodDeclarationVisitorClass"));
        classesInProject.put("MethodCoverageVisitorTest", theTestClass);

        MethodCoverageVisitor mcv = new MethodCoverageVisitor(classesInProject);
        cu.accept(mcv, null);
        Map<String, ClassCover> coveredClasses = mcv.getCoveredClasses();

        List<ProcessedMethodCall> methodCalls = new ArrayList<ProcessedMethodCall>();
        methodCalls.add(new ProcessedMethodCall("ClassCover", "getName", new ArrayList<String>()));

        assertThat(coveredClasses.get("MethodCoverageVisitorTest").getMethods().values(), hasItems(
                new MethodCover("MethodCoverageVisitorTest", "void", "should_find_classes_in_compilation_unit",
                        new ArrayList<ReferenceType>(), methodCalls)
        ));
    }
    
    @Test
    public void should_support_overloaded_method_declarations() throws FileNotFoundException, ParseException {
    	FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodCoverageVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		MethodCoverageVisitor mcv = new MethodCoverageVisitor(new HashMap<String, ClassType>());
        cu.accept(mcv, null);
        ClassCover classCover = mcv.getCoveredClasses().get("MethodCoverageVisitor");
        
        List<ReferenceType> params1 = new ArrayList<ReferenceType>();
        params1.add(new ReferenceType("MethodDeclaration", "n"));
        params1.add(new ReferenceType("ClassCover", "arg"));
        
        List<ReferenceType> params2 = new ArrayList<ReferenceType>();
        params2.add(new ReferenceType("ClassOrInterfaceDeclaration", "n"));
        params2.add(new ReferenceType("ClassCover", "arg"));
        
        List<ReferenceType> params3 = new ArrayList<ReferenceType>();
        params3.add(new ReferenceType("CompilationUnit", "arg0"));
        params3.add(new ReferenceType("ClassCover", "arg1"));
        
		assertThat(classCover.getMethods().values(), hasItems(
        		new MethodCover("MethodCoverageVisitor", "void", "visit", params1, new ArrayList<ProcessedMethodCall>()),
        		new MethodCover("MethodCoverageVisitor", "void", "visit", params2, new ArrayList<ProcessedMethodCall>()),
        		new MethodCover("MethodCoverageVisitor", "void", "visit", params3, new ArrayList<ProcessedMethodCall>())
        ));
    }
}
