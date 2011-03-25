package no.citrus.localprioritization;

public class ReferenceType {

	private String type;
	private String variableName;
	
	public ReferenceType(String type, String variableName) {
		this.type = type;
		this.variableName = variableName;
	}

	public String getType() {
		return type;
	}

	public String getVariableName() {
		return variableName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ReferenceType)) return false;
		
		ReferenceType rt = (ReferenceType) obj;
		return this.type == rt.getType() && this.variableName == rt.getVariableName();
	}
}
