package no.citrus.localprioritization.model;

public class MethodCall {

    private String scope;
    private final String methodName;

	public MethodCall(String scope, String methodName) {
        this.scope = scope;
        this.methodName = methodName;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCall that = (MethodCall) o;

        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scope != null ? scope.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        return result;
    }

    public String getMethodName() {
		return methodName;
	}

    public String getScope() {
        return scope;
    }
}
