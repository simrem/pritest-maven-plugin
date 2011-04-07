package no.citrus.localprioritization.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sveinung
 * Date: 4/7/11
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessedMethodCall extends MethodCall {
    private String className;
    private List<String> parameters;

    public ProcessedMethodCall(String className, String methodName, List<String> parameters) {
        super(methodName);
        this.className = className;
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProcessedMethodCall that = (ProcessedMethodCall) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    public String getClassName() {
        return className;
    }
}
