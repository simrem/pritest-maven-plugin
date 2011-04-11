package no.citrus.localprioritization.visitor;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import no.citrus.localprioritization.model.ReferenceType;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class FieldVisitorTest {

    @Test
    public void should_find_fields_in_class() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/model/ReferenceType.java");
		CompilationUnit cu = JavaParser.parse(fis);

		TypeDeclaration td = cu.getTypes().get(0);

        FieldVisitor fieldVisitor = new FieldVisitor();
        td.accept(fieldVisitor, null);
        Map<String, ReferenceType> fields = fieldVisitor.getFields();

        assertThat(fields.values(), hasItems(new ReferenceType("String", "variableName"),
        		new ReferenceType("String", "variableName")));
    }
}
