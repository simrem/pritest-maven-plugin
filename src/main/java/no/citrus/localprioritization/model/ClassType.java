package no.citrus.localprioritization.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassType {

	private String name;
    private List<MethodDecl> methodDeclarations;
    private Map<String, MethodDecl> methodDeclMap;
    private Map<String, ReferenceType> fields;
	private List<ClassType> innerClasses;
	private String packageName;
    private String superClass;

    public ClassType(String packageName, String className, String superClass) {
		this.packageName = packageName;
		this.name = className;
        this.superClass = superClass;
        this.fields = new HashMap<String, ReferenceType>();
        this.methodDeclarations = new ArrayList<MethodDecl>();
        this.methodDeclMap = new HashMap<String, MethodDecl>();
        this.innerClasses = new ArrayList<ClassType>();

        fields.put("this", new ReferenceType(this.name, "this"));
	}

	public String getName() {
		return this.name;
	}
    
    public Map<String, MethodDecl> getMethodDeclarations() {
        return methodDeclMap;
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

    public String getSuperClass() {
        return superClass;
    }

    public void putMethodDeclaration(MethodDecl methodDeclaration) {
        this.methodDeclMap.put(MethodDecl.createUniqueKeyForClass(methodDeclaration), methodDeclaration);
    }
}
