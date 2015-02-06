package field.dto;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import field.utils.ComUtils;

public class IngoreTable {
	private String name;
	private String desc;
	private Element ele;
	private List<IngoreField> fields = new ArrayList<IngoreField>();

	public List<IngoreField> getFields() {
		return fields;
	}

	public void setFields(List<IngoreField> fields) {
		this.fields = fields;
	}

	public void addField(IngoreField field) {
		this.fields.add(field);
	}

	public IngoreTable(String name, String desc, Element ele) {
		this.name = name;
		this.desc = desc;
		this.ele = ele;
	}

	public IngoreTable(String name, String desc, List<IngoreField> fields) {
		this.name = name;
		this.desc = desc;
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public String toString() {
		StringBuffer extend = new StringBuffer();
		if (this.getEle() != null) {
			extend.append("[È«²¿]");
		} else {
			if (!this.getFields().isEmpty()) {
				extend.append("[");
				for (IngoreField field : this.getFields()) {
					extend.append(field.toString()).append(",");
				}
				extend.deleteCharAt(extend.length() - 1);
				extend.append("]");
			}
		}
		return name + "-" + desc + extend.toString();
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Element getEle() {
		return ele;
	}

	public void setEle(Element ele) {
		this.ele = ele;
	}

	public String getXml() {
		if (getEle() != null) {
			return ele.asXML();
		}
		if (fields.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (IngoreField f : fields) {
			String fx = f.getEle().asXML();
			if (!ComUtils.isStrEmpty(fx)) {
				sb.append(fx).append("\n");
			}

		}
		return sb.toString();
	}

}
