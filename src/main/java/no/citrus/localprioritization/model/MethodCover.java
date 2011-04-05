package no.citrus.localprioritization.model;

public class MethodCover {
    private String returnType;
    private String methodName;

    public MethodCover(String returnType, String methodName) {
        this.returnType = returnType;
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCover that = (MethodCover) o;

        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (returnType != null ? !returnType.equals(that.returnType) : that.returnType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = returnType != null ? returnType.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        return result;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }
}
