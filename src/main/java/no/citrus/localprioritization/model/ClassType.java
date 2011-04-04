package no.citrus.localprioritization.model;

import java.util.ArrayList;
import java.util.List;

public class ClassType {

	private String name;
    private List<MethodDecl> methodDeclarations;
    private List<ReferenceType> fields;
	private List<ClassType> innerClasses;
	private String packageName;

    public ClassType(String packageName, String className) {
		this.packageName = packageName;
		this.name = className;
        this.fields = new ArrayList<ReferenceType>();
        this.methodDeclarations = new ArrayList<MethodDecl>();
        this.innerClasses = new ArrayList<ClassType>();
	}

	public String getName() {
		return this.name;
	}

    public List<MethodDecl> getMethodDeclarations() {
        return methodDeclarations;
    }

    public List<ReferenceType> getFields() {
        return fields;
    }

	public List<ClassType> getInnerClasses() {
		return this.innerClasses;
	}

	public String getPackageName() {
		return packageName;
	}
}
