package no.citrus.localprioritization;

public class MethodCall {

	private final String methodName;

	public MethodCall(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodCall other = (MethodCall) obj;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}


}
