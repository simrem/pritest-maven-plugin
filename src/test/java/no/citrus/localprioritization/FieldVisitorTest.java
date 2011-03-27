package no.citrus.localprioritization;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class FieldVisitorTest {

    @Test
    public void shouldFindFieldsInClass() throws FileNotFoundException, ParseException {
        FileInputStream fis = new FileInputStream("src/main/java/no/citrus/localprioritization/ReferenceType.java");
		CompilationUnit cu = JavaParser.parse(fis);

		TypeDeclaration td = cu.getTypes().get(0);

        FieldVisitor fieldVisitor = new FieldVisitor();
        td.accept(fieldVisitor, null);
        List<ReferenceType> fields = fieldVisitor.getFields();

        assertThat(fields, hasItem(new ReferenceType("String", "type")));
        assertThat(fields, hasItem(new ReferenceType("String", "variableName")));
    }
}
