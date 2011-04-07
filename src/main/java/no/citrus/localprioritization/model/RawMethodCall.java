package no.citrus.localprioritization.model;

public class RawMethodCall extends MethodCall {

    private String scope;

    public RawMethodCall(String scope, String methodName) {
        super(methodName);
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RawMethodCall that = (RawMethodCall) o;

        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        return result;
    }

    public String getScope() {
        return scope;
    }
}
