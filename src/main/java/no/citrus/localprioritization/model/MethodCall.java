package no.citrus.localprioritization.model;

/**
 * Created by IntelliJ IDEA.
 * User: sveinung
 * Date: 4/7/11
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MethodCall {

    protected final String methodName;

    public MethodCall(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCall that = (MethodCall) o;

        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return methodName != null ? methodName.hashCode() : 0;
    }

    public String getMethodName() {
		return methodName;
	}
}
