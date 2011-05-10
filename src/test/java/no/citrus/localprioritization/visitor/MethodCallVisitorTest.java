package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import no.citrus.localprioritization.model.RawMethodCall;
import no.citrus.localprioritization.model.ReferenceType;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MethodCallVisitorTest {
	
	private List<RawMethodCall> methodCalls;

	@Before
	public void setup() throws FileNotFoundException, ParseException {
		FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodCallVisitor.java");
		CompilationUnit cu = JavaParser.parse(fis);
		
		TypeDeclaration td = cu.getTypes().get(0);
		MethodDeclaration method = (MethodDeclaration) td.getMembers().get(4);
		
		Map<String, ReferenceType> localVariables = new HashMap<String, ReferenceType>();
		localVariables.put("n", new ReferenceType("MethodCallExpr", "n"));
		localVariables.put("methodCall", new ReferenceType("RawMethodCall", "methodCall"));
		
		Map<String, ReferenceType> fieldVariables = new HashMap<String, ReferenceType>();
		fieldVariables.put("methodCalls", new ReferenceType("List", "methodCalls"));
		
		MethodCallVisitor mcv = new MethodCallVisitor(localVariables, fieldVariables);
		method.accept(mcv, null);

		methodCalls = mcv.getRawMethodCalls();
	}
	
	@Test
	public void should_find_method_calls_in_method() {
        assertThat(methodCalls, hasItems(
				new RawMethodCall("n", "getName", new ArrayList<String>())
		));
	}
	
	@Test
	public void should_discover_the_type_of_parameters_where_local_variables_are_refered() {
		List<String> methodCallsAddParameters = new ArrayList<String>();
		methodCallsAddParameters.add("RawMethodCall");
		
		assertThat(methodCalls, hasItems(
				new RawMethodCall("methodCalls", "add", methodCallsAddParameters)
		));
	}

    @Test
    public void should_support_this_calls_as_arguments() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/visitor/MethodCoverageVisitor.java");
        CompilationUnit cu = JavaParser.parse(fis);

        Map<String,ReferenceType> localVariables = new HashMap<String,ReferenceType>();
        localVariables.put("currentClass", new ReferenceType("ClassCover", "currentClass"));

        Map<String,ReferenceType> fieldVariables = new HashMap<String,ReferenceType>();
        fieldVariables.put("this", new ReferenceType("MethodCoverageVisitor", "this"));
        
        MethodCallVisitor mcv = new MethodCallVisitor(localVariables, fieldVariables);
        cu.getTypes().get(0).getMembers().get(5).accept(mcv, null);
        List<RawMethodCall> rawMethodCalls = mcv.getRawMethodCalls();

        List<String> parameters = new ArrayList<String>();
        parameters.add("MethodCoverageVisitor");
        parameters.add("ClassCover");

        assertThat(rawMethodCalls, hasItems(
                new RawMethodCall("member", "accept", parameters)
        ));
    }
}
