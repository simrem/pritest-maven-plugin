package no.citrus.localprioritization.model;

import java.util.List;

public abstract class MethodCall {

    protected String methodName;
    protected List<String> parameters;

    public MethodCall(String methodName, List<String> parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCall that = (MethodCall) o;

        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    public String getMethodName() {
		return methodName;
	}

    public List<String> getParameters() {
        return parameters;
    }
}
