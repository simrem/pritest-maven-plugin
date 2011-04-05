package no.citrus.localprioritization.model;

import java.util.HashMap;
import java.util.Map;

public class ClassCover {

    private String className;
    private Map<String, MethodCover> methods;

    public ClassCover(String className) {
        this.className = className;
        methods = new HashMap<String, MethodCover>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassCover that = (ClassCover) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (methods != null ? !methods.equals(that.methods) : that.methods != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        return result;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, MethodCover> getMethods() {
        return methods;
    }
}
