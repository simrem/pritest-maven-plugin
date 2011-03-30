package no.citrus.localprioritization;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterfaceDeclarationVisitor extends GenericVisitorAdapter<ClassType, ClassType> {

    private List<ClassType> classes;

    public ClassOrInterfaceDeclarationVisitor() {
        this.classes = new ArrayList<ClassType>();
    }

    @Override
	public ClassType visit(ClassOrInterfaceDeclaration cid, ClassType classType) {
		String className = cid.getName();

        ClassType newClass = new ClassType(className);

        ClassOrInterfaceDeclarationVisitor cidVisitor = new ClassOrInterfaceDeclarationVisitor();

        if (cid.getMembers() != null) {
            for (BodyDeclaration bd : cid.getMembers()) {
                bd.accept(cidVisitor, newClass);
            }
        }

        classes.add(newClass);

        newClass.getInnerClasses().addAll(cidVisitor.getClasses());
        
        return classType;
	}

    @Override
	public ClassType visit(FieldDeclaration fieldDeclaration, ClassType classType) {
    	FieldVisitor fv = new FieldVisitor();
    	fieldDeclaration.accept(fv, classType);
    	
    	classType.getFields().addAll(fv.getFields());
    	
		return classType;
	}

	@Override
	public ClassType visit(MethodDeclaration methodDeclaration, ClassType classType) {
		MethodDeclarationVisitor mdv = new MethodDeclarationVisitor();
		methodDeclaration.accept(mdv, classType);
		
		classType.getMethodDeclarations().addAll(mdv.getMethodDeclarations());
		
		return classType;
	}

	public List<ClassType> getClasses() {
        return classes;
    }
}
