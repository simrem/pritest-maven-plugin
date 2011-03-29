package no.citrus.localprioritization;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterfaceDeclarationVisitor extends VoidVisitorAdapter<Object> {

    private List<ClassType> classes;

    public ClassOrInterfaceDeclarationVisitor() {
        this.classes = new ArrayList<ClassType>();
    }

    @Override
	public void visit(ClassOrInterfaceDeclaration cid, Object obj) {
		String className = cid.getName();

        ClassType newClass = new ClassType(className);

        ClassOrInterfaceDeclarationVisitor cidVisitor = new ClassOrInterfaceDeclarationVisitor();

        if (cid.getMembers() != null) {
            for (BodyDeclaration bd : cid.getMembers()) {
                bd.accept(cidVisitor, null);
            }
        }

        //FieldVisitor fv = new FieldVisitor();
        //cid.accept(fv, null);

        classes.add(newClass);

        classes.addAll(cidVisitor.getClasses());
	}

    public List<ClassType> getClasses() {
        return classes;
    }
}
