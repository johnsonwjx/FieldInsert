package field.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TableDto implements Cloneable {
	private String authmode = "0";
	// strut
	private String fathertablename = "";
	private List<FieldDto> fields = new LinkedList<FieldDto>();
	private String groupkeyfield = "";
	private List<IndexDto> indexs = new ArrayList<IndexDto>();
	private String keyfieldname = "";
	private String mode = "0";
	private int sortnum = -1;
	private boolean strut = true;
	private String table_desc = "";
	private String table_name = "";

	public TableDto() {
	}

	public TableDto(int sortnum) {
		this.sortnum = sortnum;
	}

	public TableDto(String table_name, String table_desc, int sortnum) {
		this.table_name = table_name;
		this.table_desc = table_desc;
		this.sortnum = sortnum;
	}

	public void addField(FieldDto field) {
		fields.add(field);
	}

	public void addField(int index, FieldDto field) {
		fields.add(index, field);
	}

	public void addIndex(IndexDto index) {
		indexs.add(index);
	}

	public TableDto clone() throws CloneNotSupportedException {
		TableDto o = null;
		o = (TableDto) super.clone();
		List<FieldDto> field2s = new LinkedList<FieldDto>();
		if (this.fields != null && !this.fields.isEmpty()) {
			for (FieldDto f : fields) {
				field2s.add(f.clone());
			}
		}
		o.setFields(field2s);
		List<IndexDto> index2s = new ArrayList<IndexDto>();
		if (this.indexs != null && !this.indexs.isEmpty()) {
			for (IndexDto i : indexs) {
				index2s.add(i.clone());
			}
		}
		o.setIndexs(index2s);
		return o;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableDto other = (TableDto) obj;
		if (table_name == null) {
			if (other.table_name != null)
				return false;
		} else if (!table_name.equals(other.table_name))
			return false;
		return true;
	}

	public String getAuthmode() {
		return authmode;
	}

	public String getFathertablename() {
		if (fathertablename == null) {
			return null;
		}
		return fathertablename.toUpperCase();
	}

	public List<FieldDto> getFields() {
		return fields;
	}

	public int getFieldSize() {
		if (fields == null) {
			return 0;
		}
		return fields.size();
	}

	public String getGroupkeyfield() {
		return groupkeyfield;
	}

	public List<IndexDto> getIndexs() {
		return indexs;
	}

	public int getIndexSizw() {
		return indexs.size();
	}

	public String getKeyfieldname() {
		return keyfieldname;
	}

	public String getMode() {
		return mode;
	}

	public int getSortnum() {
		return sortnum;
	}

	public String getTable_desc() {
		return table_desc;
	}

	public String getTable_name() {
		if (table_name != null) {
			table_name = table_name.toUpperCase();
		}
		return table_name;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((table_name == null) ? 0 : table_name.hashCode());
		return result;
	}

	public boolean isStrut() {
		return strut;
	}

	public void removeField(FieldDto field) {
		this.fields.remove(field);
	}

	public void removeField(int index) {
		fields.remove(index);
	}

	public void setAuthmode(String authmode) {
		this.authmode = authmode;
	}

	public void setFathertablename(String fathertablename) {
		if (fathertablename == null) {
			return;
		}
		this.fathertablename = fathertablename.toUpperCase();
	}

	public void setFields(List<FieldDto> fields) {
		this.fields = fields;
	}

	public void setGroupkeyfield(String groupkeyfield) {
		this.groupkeyfield = groupkeyfield;
	}

	public void setIndexs(List<IndexDto> indexs) {
		this.indexs = indexs;
	}

	public void setKeyfieldname(String keyfieldname) {
		this.keyfieldname = keyfieldname;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setSortnum(int sortnum) {
		this.sortnum = sortnum;
	}

	public void setStrut(boolean strut) {
		this.strut = strut;
	}

	public void setTable_desc(String table_desc) {
		this.table_desc = table_desc;
	}

	public void setTable_name(String table_name) {
		if (table_name != null) {
			table_name = table_name.toUpperCase();
		}
		this.table_name = table_name;
	}

	public String toString() {
		return (strut ? "" : "(strut√ª”–)") + this.getSortnum() + ". " + this.getTable_desc() + "-" + this.getTable_name();
	}
}
