package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.GenericVisitorAdapter;
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
            FieldTypeVisitor ftv = new FieldTypeVisitor();
            n.getType().accept(ftv, null);
            String type = ftv.getName();

            if (n.getVariables() != null) {
                for (VariableDeclarator vd : n.getVariables()) {
                    String name = vd.accept(new FieldVariableVisitor(), null);

                    this.fields.add(new ReferenceType(type, name));
                }
            }
        }
    }

    private class FieldVariableVisitor extends GenericVisitorAdapter<String, Object> {

        @Override
        public String visit(VariableDeclarator n, Object arg) {
            if (n.getId() != null) {
                return n.getId().accept(this, null);
            } else {
                return null;
            }
        }

        @Override
        public String visit(VariableDeclaratorId n, Object arg) {
            return n.getName();
        }
    }

    private class FieldTypeVisitor extends VoidVisitorAdapter<Object> {

        private String name;

        @Override
        public void visit(ClassOrInterfaceType n, Object arg) {
            this.name = n.getName();
        }

        public String getName() {
            return this.name;
        }
    }
}
