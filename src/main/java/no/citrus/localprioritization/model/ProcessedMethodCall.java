package no.citrus.localprioritization.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessedMethodCall extends MethodCall {
    private String className;

    public ProcessedMethodCall(String className, String methodName, List<String> parameters) {
        super(methodName, parameters);
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProcessedMethodCall that = (ProcessedMethodCall) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (className != null ? className.hashCode() : 0);
        return result;
    }

    public String getClassName() {
        return className;
    }
}
