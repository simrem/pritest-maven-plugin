package no.citrus.localprioritization.visitor;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import no.citrus.localprioritization.model.ClassType;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterfaceDeclarationVisitor extends GenericVisitorAdapter<ClassType, ClassType> {

    private List<ClassType> types;
	private final String packageName;

    public ClassOrInterfaceDeclarationVisitor(String packageName) {
        this.packageName = packageName;
		this.types = new ArrayList<ClassType>();
    }

    @Override
	public ClassType visit(ClassOrInterfaceDeclaration cid, ClassType classType) {
		String className = cid.getName();

        ClassType newClass = new ClassType(this.packageName, className);

        ClassOrInterfaceDeclarationVisitor cidVisitor =
        	new ClassOrInterfaceDeclarationVisitor(this.packageName + "." + newClass.getName());

        if (cid.getMembers() != null) {
            for (BodyDeclaration bd : cid.getMembers()) {
                bd.accept(cidVisitor, newClass);
            }
        }

        types.add(newClass);

        newClass.getInnerClasses().addAll(cidVisitor.getTypes());
        
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

	public List<ClassType> getTypes() {
        return types;
    }
}
