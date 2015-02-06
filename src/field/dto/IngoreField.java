package field.dto;

import org.dom4j.Element;

public class IngoreField {
	private String name;
	private String desc;
	private Element ele;

	public IngoreField(String name, String desc, Element ele) {
		this.name = name;
		this.desc = desc;
		this.ele = ele;
	}

	public String toString() {
		return name + "-" + desc;
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

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Element getEle() {
		return ele;
	}

	public void setEle(Element ele) {
		this.ele = ele;
	}
}
