package no.citrus.localprioritization;

import java.util.List;

public class ClassType {

	private List<ReferenceType> fields;
	private String name;

	public ClassType(List<ReferenceType> fields, String name) {
		this.fields = fields;
		this.name = name;
	}

	public List<ReferenceType> getFields() {
		return this.fields;
	}

	public String getName() {
		return this.name;
	}

}
