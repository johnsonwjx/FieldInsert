package field.dto;

public class FieldDto implements Cloneable {
	private String field_dec = "";
	private String field_default = "";
	private String field_desc = "";
	private String field_length = "";
	private String field_name = "";
	private String field_null = "";
	private String field_type = "C";

	private String forignkey = "";
	private int sortnum = -1;
	private String type = "2";

	public FieldDto() {
	}

	public FieldDto(String field_name, String field_desc, String field_type, String field_length, String field_dec, String field_null, String field_default, int sortnum) {
		this.field_name = field_name;
		this.field_desc = field_desc;
		this.field_type = field_type;
		this.field_length = field_length;
		this.field_dec = field_dec;
		this.field_null = field_null;
		this.field_default = field_default;
		this.sortnum = sortnum;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field_name == null) ? 0 : field_name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldDto other = (FieldDto) obj;
		if (field_name == null) {
			if (other.field_name != null)
				return false;
		} else if (!field_name.equals(other.field_name))
			return false;
		return true;
	}

	public String getField_dec() {
		return field_dec;
	}

	public String getField_default() {
		return field_default;
	}

	public String getField_desc() {
		return field_desc;
	}

	public String getField_length() {
		return field_length;
	}

	public String getField_name() {
		if (field_name != null) {
			field_name = field_name.toUpperCase();
		}
		return field_name;
	}

	public String getField_null() {
		return field_null;
	}

	public String getField_type() {
		if (field_type != null) {
			field_type = field_type.toUpperCase();
		}
		return field_type;
	}

	public String getForignkey() {
		return forignkey;
	}

	public int getSortnum() {
		return sortnum;
	}

	public String getType() {
		return type;
	}

	public void setField_dec(String field_dec) {
		this.field_dec = field_dec;
	}

	public void setField_default(String field_default) {
		this.field_default = field_default;
	}

	public void setField_desc(String field_desc) {
		this.field_desc = field_desc;
	}

	public void setField_length(String field_length) {
		this.field_length = field_length;
	}

	public void setField_name(String field_name) {
		if (field_name != null) {
			field_name = field_name.toUpperCase();
		}
		this.field_name = field_name;
	}

	public void setField_null(String field_null) {
		this.field_null = field_null;
	}

	public void setField_type(String field_type) {
		if (field_type != null) {
			field_type = field_type.toUpperCase();
		}
		this.field_type = field_type;
	}

	public void setForignkey(String forignkey) {
		this.forignkey = forignkey;
	}

	public void setSortnum(int sortnum) {
		this.sortnum = sortnum;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return this.getSortnum() + ". " + this.getField_desc() + "-" + this.getField_name();
	}

	public FieldDto clone() throws CloneNotSupportedException {
		FieldDto o = null;
		o = (FieldDto) super.clone();
		return o;
	}
}
