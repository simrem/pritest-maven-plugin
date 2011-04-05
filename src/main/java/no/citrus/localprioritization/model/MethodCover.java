package no.citrus.localprioritization.model;

public class MethodCover {
    private String returnType;
    private String methodName;

    public MethodCover(String returnType, String methodName) {
        this.returnType = returnType;
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }
}
