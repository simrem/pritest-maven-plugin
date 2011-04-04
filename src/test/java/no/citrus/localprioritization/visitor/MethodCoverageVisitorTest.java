package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import no.citrus.localprioritization.model.ClassCover;
import no.citrus.localprioritization.model.ClassType;
import no.citrus.localprioritization.model.MethodDecl;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCoverageVisitorTest {

    @Test
    @Ignore
    public void should_find_references_to_methods_in_the_current_project() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodDeclarationVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);

        Map<String, ClassType> classesInProject = new HashMap<String, ClassType>();

        ClassType callingClass = new ClassType("no.citrus.localprioritization.visitor", "MethodDeclarationVisitor");
        //  Fyll ut metodar

        ClassType firstClass = new ClassType("japa.parser.ast.body", "MethodDeclaration");
        firstClass.getMethodDeclarations().add(new MethodDecl("String", "getName", new ArrayList<String>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("Type", "getType", new ArrayList<String>()));
        firstClass.getMethodDeclarations().add(new MethodDecl("List", "getParameters", new ArrayList<String>()));

        ClassType secondClass = new ClassType("japa.parser.ast.body", "Parameter");
        //  Fyll ut metodar

        classesInProject.put("MethodDeclaration", firstClass);

        MethodCoverageVisitor mvc = new MethodCoverageVisitor(classesInProject);
        cu.getTypes().get(0).getMembers().get(2).accept(mvc, null);
        Map<String, ClassCover> coveredClasses = mvc.getCoveredClasses();

        assertThat(coveredClasses.values(), hasItems(
                new ClassCover("ReturnTypeVisitor"),
                new ClassCover("ParameterVisitor")
        ));
    }
}
