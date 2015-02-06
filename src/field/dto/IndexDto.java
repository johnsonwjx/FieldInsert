package field.dto;

public class IndexDto implements Cloneable {
	private String name;
	private String fields = "";
	private String type = "I";

	public IndexDto() {
	}

	public IndexDto(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public IndexDto(String name, String type, String fields) {
		this.name = name;
		this.fields = fields;
		this.type = type;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexDto other = (IndexDto) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IndexDto clone() throws CloneNotSupportedException {
		IndexDto o = null;
		o = (IndexDto) super.clone();
		return o;
	}
}
