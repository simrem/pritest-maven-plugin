package no.citrus.localprioritization.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassType {

	private String name;
    private List<MethodDecl> methodDeclarations;
    private Map<String, ReferenceType> fields;
	private List<ClassType> innerClasses;
	private String packageName;

    public ClassType(String packageName, String className) {
		this.packageName = packageName;
		this.name = className;
        this.fields = new HashMap<String, ReferenceType>();
        this.methodDeclarations = new ArrayList<MethodDecl>();
        this.innerClasses = new ArrayList<ClassType>();
	}

	public String getName() {
		return this.name;
	}

    public List<MethodDecl> getMethodDeclarations() {
        return methodDeclarations;
    }

    public Map<String, ReferenceType> getFields() {
        return fields;
    }

	public List<ClassType> getInnerClasses() {
		return this.innerClasses;
	}

	public String getPackageName() {
		return packageName;
	}
}
