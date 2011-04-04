package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import no.citrus.localprioritization.model.ReferenceType;

import java.util.ArrayList;
import java.util.List;

public class FieldVisitor extends VoidVisitorAdapter<Object> {
    private List<ReferenceType> fields;

    public FieldVisitor() {
        this.fields = new ArrayList<ReferenceType>();
    }

    public List<ReferenceType> getFields() {
        return fields;
    }

    @Override
    public void visit(FieldDeclaration n, Object arg) {
        if (n.getType() != null) {
            TypeVisitor tv = new TypeVisitor();
            n.getType().accept(tv, null);
            String type = tv.getName();

            if (n.getVariables() != null) {
                for (VariableDeclarator vd : n.getVariables()) {
                    String name = vd.accept(new VariableVisitor(), null);

                    this.fields.add(new ReferenceType(type, name));
                }
            }
        }
    }
}
