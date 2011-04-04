package no.citrus.localprioritization.model;

public class ClassCover {

    private String className;

    public ClassCover(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassCover that = (ClassCover) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }

    public String getClassName() {
        return className;
    }
}
