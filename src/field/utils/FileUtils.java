package field.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import field.Global;
import field.dto.FieldDto;
import field.dto.IndexDto;
import field.dto.IngoreField;
import field.dto.IngoreTable;
import field.dto.TableDto;

public class FileUtils {
	public static final Logger logger = LogManager.getLogger(FileUtils.class.getName());

	/**
	 * 创建临时表 使字符串全小写 避免读入格式不一致有大小写都有
	 * 
	 * @param file
	 */
	public static void createTempXmlFile(File file) {
		BufferedReader tempreader = null;
		PrintWriter tempwriter = null;
		try {
			File tempFile = new File(Global.TEMP_FILE);
			tempFile.deleteOnExit();
			String encoding = "UTF-8";
			tempreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			String line = null;
			try {
				while ((line = tempreader.readLine()) != null) {
					line = line.toLowerCase();
					if (line.indexOf("encoding") >= 0) {
						if (line.indexOf("gbk") >= 0) {
							encoding = "GBK";
						} else if (line.indexOf("gb2312") >= 0) {
							encoding = "GB2312";
						}
						break;
					}
				}
			} finally {
				if (tempreader != null) {
					tempreader.close();
					tempreader = null;
				}
			}
			// 重建立reader
			tempreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			tempwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile), encoding));
			while ((line = tempreader.readLine()) != null) {
				line = line.toLowerCase();
				tempwriter.write(line);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Global.MAIN, "创建临时文件错误");
			throw new ExceptionInInitializerError(e);
		} finally {
			if (tempreader != null) {
				try {
					tempreader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (tempwriter != null) {
				tempwriter.close();
			}

		}

	}

	/**
	 * 建表xml表格element 转 表对象
	 * 
	 * @param tableEle
	 * @param sortNum
	 * @return
	 * @throws Exception
	 */
	public static TableDto element2TableDto(Element tableEle, int sortNum) throws Exception {
		String table_name = tableEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.TABLE_NAME_TAG) + "]").getText();
		String table_desc = tableEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.TABLE_DESC_TAG) + "]").getText();
		TableDto table = new TableDto(table_name, table_desc, sortNum);
		Element indexsEle = (Element) tableEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.INDEX_TAG) + "]");
		if (indexsEle != null && indexsEle.hasContent()) {
			List<Element> indexEles = indexsEle.elements();
			if (!indexEles.isEmpty()) {
				for (Element indexEle : indexEles) {
					IndexDto index = FileUtils.element2IndexDto(indexEle);
					table.addIndex(index);
				}
			}
		}
		for (Element fieldEle : (List<Element>) tableEle.selectNodes("*[" + Global.cover_node_lowercase(Global.FIELD_TAG) + "]")) {
			FieldDto field = element2FieldDto(fieldEle, table.getFieldSize() + 1);
			table.addField(field);
		}
		return table;
	}

	/**
	 * element 转 Index对象
	 * 
	 * @param indexEle
	 * @return
	 */
	public static IndexDto element2IndexDto(Element indexEle) {
		String name = indexEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.INDEX_NAME_TAG) + "]").getText();
		String type = indexEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.INDEX_TYPE_TAG) + "]").getText();
		String fields = indexEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.INDEX_FIELDS_TAG) + "]").getText();
		IndexDto index = new IndexDto(name, type);
		if (!ComUtils.isStrEmpty(fields)) {
			index.setFields(fields.toUpperCase());
		}
		return index;
	}

	/**
	 * 建表xml转字段
	 * 
	 * @param fieldEle
	 * @param sortNum
	 * @return
	 * @throws Exception
	 */
	public static FieldDto element2FieldDto(Element fieldEle, int sortNum) throws Exception {
		String field_name = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_NAME_TAG) + "]").getText();
		String field_desc = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_DESC_TAG) + "]").getText();
		String field_type = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_TYPE_TAG) + "]").getText();
		String field_length = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_LENGTH_TAG) + "]").getText();
		String field_dec = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_DEC_TAG) + "]").getText();
		String field_null = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_NULL_TAG) + "]").getText();
		String field_default = fieldEle.selectSingleNode("*[" + Global.cover_node_lowercase(Global.PARA_TAG) + " and " + Global.cover_attr_lowercase("name", Global.FIELD_DEFAULT_TAG) + "]").getText();
		FieldDto field = new FieldDto(field_name, field_desc, field_type, field_length, field_dec, field_null, field_default, sortNum);
		return field;
	}

	/**
	 * 写出建表文件
	 * 
	 * @param model
	 * @param file
	 * @throws Exception
	 */
	public static void writeInnitDataXML(DefaultListModel model, File file) throws Exception {
		XMLWriter writer = null;
		try {
			Document doc = DocumentHelper.createDocument();
			Element rootElement = DocumentHelper.createElement("InitDataList");
			doc.setRootElement(rootElement);
			Element InitData = rootElement.addElement("InitData");
			InitData.addAttribute("name", "DDATA");
			for (int i = 0; i < model.getSize(); i++) {
				TableDto table = (TableDto) model.getElementAt(i);
				Element tableEle = createInitDataTableEle(table);
				InitData.add(tableEle);
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Global.Encoding_initData);
			writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(doc);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}

	/**
	 * 表对象转建表文件Element
	 * 
	 * @param table
	 * @return
	 */
	public static Element createInitDataTableEle(TableDto table) {
		Element tableEle = DocumentHelper.createElement("Table");
		tableEle.addElement("TABLE_NAME").setText(table.getTable_name());
		tableEle.addElement("TABLE_DESC").setText(table.getTable_desc());
		Element indexs = tableEle.addElement("indexs");
		if (!table.getIndexs().isEmpty()) {
			for (IndexDto index : table.getIndexs()) {
				Element indexEle = createInnitDataIndexEle(index);
				indexs.add(indexEle);
			}
		}
		if (!table.getFields().isEmpty()) {
			for (FieldDto field : table.getFields()) {
				Element fieldEle = createInitDataFieldEle(field);
				tableEle.add(fieldEle);
			}
		}
		return tableEle;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public static Element createInnitDataIndexEle(IndexDto index) {
		Element indexEle = DocumentHelper.createElement("index");
		indexEle.addElement("name").setText(index.getName());
		indexEle.addElement("fields").setText(index.getFields());
		indexEle.addElement("type").setText(index.getType());
		return indexEle;
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public static Element createInitDataFieldEle(FieldDto field) {
		Element fieldEle = DocumentHelper.createElement("Field");
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_NAME_TAG.toUpperCase()).setText(field.getField_name());
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_DESC_TAG.toUpperCase()).setText(field.getField_desc());
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_TYPE_TAG.toUpperCase()).setText(field.getField_type());
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_LENGTH_TAG.toUpperCase()).setText(field.getField_length());
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_DEC_TAG.toUpperCase()).setText(field.getField_dec());
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_NULL_TAG.toUpperCase()).setText(field.getField_null());
		fieldEle.addElement("Para").addAttribute("name", Global.FIELD_DEFAULT_TAG.toUpperCase()).setText(field.getField_default());
		return fieldEle;
	}

	public static List<String> updatePropByStrutElement(List<TableDto> tableDtos, List<Element> tableEles) throws Exception {
		int returnValue = -1;
		if (tableDtos == null || tableEles == null || tableDtos.isEmpty() || tableEles.isEmpty()) {
			return null;
		}
		logger.debug("size:" + tableDtos.size() + ":" + tableEles.size());

		if (tableDtos.size() != tableEles.size()) {
			returnValue = JOptionPane.showConfirmDialog(Global.MAIN, "当前表格数目和导入表格数目不同,是否继续?", "", JOptionPane.OK_CANCEL_OPTION);
			if (returnValue != JOptionPane.OK_OPTION) {
				return null;
			}
		}
		returnValue = JOptionPane.showConfirmDialog(Global.MAIN, "需要以Strut为标准重新排序和设中文描述吗", "", JOptionPane.OK_CANCEL_OPTION);
		// 以Strut 为标准
		boolean useStrutStandard = returnValue == JOptionPane.OK_OPTION;

		List<IngoreTable> ingoreTables = new ArrayList<IngoreTable>();

		List<String> strutSort = new ArrayList<String>();
		int insertIndex = 0;
		int sturtIndex = -1;
		for (Element ele : tableEles) {
			sturtIndex++;
			String tablename = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_TABLENAME_TAG).toUpperCase());
			String cname = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_TABLECNAME_TAG));
			String fathertablename = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_FATHERTABLENAME_TAG));
			String mode = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_MODE_TAG));
			String authmode = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_AUTHMODE_TAG));
			String keyfieldname = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_KEYFIELDNAME_TAG));
			String groupkeyfield = ComUtils.coverNULL(ele.attributeValue(Global.STRUT_GROUPKEYFIELD_TAG));

			TableDto curTable = null;
			int currentIndex = -1;
			TableDto tempDto = null;
			// 先直接取，有可能对应
			if (sturtIndex < tableDtos.size()) {
				tempDto = tableDtos.get(sturtIndex);
				if (tempDto.getTable_name().equals(tablename)) {
					curTable = tempDto;
					currentIndex = sturtIndex;
				}
			}

			// 没找到curTable,即不对应遍历
			if (curTable == null) {
				for (int i = 0; i < tableDtos.size(); i++) {
					tempDto = tableDtos.get(i);
					if (tempDto.getTable_name().equals(tablename)) {
						curTable = tempDto;
						currentIndex = i;
						break;
					}
				}
			}
			if (curTable == null) {
				ingoreTables.add(new IngoreTable(tablename, cname, ele));
				continue;
			}

			// 要改的都克隆一份
			curTable = curTable.clone();

			curTable.setFathertablename(fathertablename);
			curTable.setMode(mode);
			curTable.setAuthmode(authmode);
			curTable.setKeyfieldname(keyfieldname.toUpperCase());
			curTable.setGroupkeyfield(groupkeyfield.toUpperCase());

			// 更新字段
			List<Element> fieldEles = ele.selectNodes("*[" + Global.cover_node_lowercase(Global.STRUT_FIELD_TAG)+"]");

			updateFieldByStrutElement(curTable, fieldEles, useStrutStandard, ingoreTables);

			// 保存strut顺序
			strutSort.add(curTable.getTable_name());
			if (!useStrutStandard) {
				continue;
			}
			curTable.setTable_name(tablename);
			curTable.setTable_desc(cname);

			// 如果当前表位置 跟要插入位置不同
			if (currentIndex != insertIndex) {
				tableDtos.remove(currentIndex);
				tableDtos.add(insertIndex, curTable);
			}
			insertIndex++;
		}

		if (tableDtos.size() > tableEles.size()) {
			for (int i = tableDtos.size() - 1; i >= tableEles.size(); i--) {
				tableDtos.get(i).setStrut(false);
			}
		}

		if (!ingoreTables.isEmpty()) {
			JOptionPane.showMessageDialog(Global.MAIN, "忽略Strut的:" + ingoreTables);
			Global.ingoreTables = ingoreTables;
		}

		return strutSort;
	}

	public static void updateFieldByStrutElement(TableDto curTable, List<Element> fieldEles, boolean useStrutStandard, List<IngoreTable> ingoreTables) {
		if (fieldEles.size() < 0) {
			return;
		}
		List<FieldDto> fields = curTable.getFields();
		int insertIndex = 0;
		int strutFieldIndex = -1;
		List<IngoreField> ingoreFields = new ArrayList<IngoreField>();
		for (Element fieldEle : fieldEles) {
			strutFieldIndex++;
			String name = ComUtils.coverNULL(fieldEle.elementText(Global.STRUT_FIELDNAME_TAG).toUpperCase());
			String cname = ComUtils.coverNULL(fieldEle.elementText(Global.STRUT_FIELDCNAME_TAG));
			String type = ComUtils.coverNULL(fieldEle.elementText(Global.STRUT_FIELDTYPE_TAG));
			String forignkey = ComUtils.coverNULL(fieldEle.elementText(Global.STRUT_FIELDFORIGNKEY_TAG));
			FieldDto curField = null;
			int currentIndex = -1;
			FieldDto tempDto = null;
			// 一般都是对应的 先直接取
			if (strutFieldIndex < fields.size()) {
				tempDto = fields.get(strutFieldIndex);
				if (tempDto.getField_name().equals(name)) {
					curField = tempDto;
					currentIndex = strutFieldIndex;
				}
			}

			if (curField == null) {
				for (int i = 0; i < fields.size(); i++) {
					tempDto = fields.get(i);
					if (name.equals(tempDto.getField_name())) {
						currentIndex = i;
						curField = tempDto;
					}
				}
			}

			if (curField == null) {
				ingoreFields.add(new IngoreField(name, cname, fieldEle));
				continue;
			}

			curField.setType(type);
			curField.setForignkey(forignkey);

			if (!useStrutStandard) {
				continue;
			}
			curField.setField_name(name);
			curField.setField_desc(cname);

			if (insertIndex != currentIndex) {
				fields.remove(currentIndex);
				fields.add(insertIndex, curField);
				curField.setSortnum(insertIndex + 1);
			}
			insertIndex++;
		}
		curTable.setFields(fields);
		if (ingoreFields.size() > 0) {
			ingoreTables.add(new IngoreTable(curTable.getTable_name(), curTable.getTable_desc(), ingoreFields));
		}
	}

	public static void writeStrutXml(DefaultListModel model, File file) throws Exception {
		XMLWriter writer = null;
		try {
			Document doc = DocumentHelper.createDocument();
			Element root = DocumentHelper.createElement("root");
			doc.setRootElement(root);
			if (!model.isEmpty()) {
				for (int i = 0; i < model.getSize(); i++) {
					TableDto table = (TableDto) model.getElementAt(i);
					Element tableEle = createStrutTableEle(table);
					root.add(tableEle);
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Global.Encoding_Strut);
			writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(doc);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public static Element createStrutTableEle(TableDto table) {
		Element tableEle = DocumentHelper.createElement(Global.STRUT_TABLE_TAG);
		tableEle.addAttribute(Global.STRUT_TABLENAME_TAG, table.getTable_name());
		tableEle.addAttribute(Global.STRUT_TABLECNAME_TAG, table.getTable_desc());
		tableEle.addAttribute(Global.STRUT_FATHERTABLENAME_TAG, table.getFathertablename());
		tableEle.addAttribute(Global.STRUT_MODE_TAG, table.getMode());
		tableEle.addAttribute(Global.STRUT_AUTHMODE_TAG, table.getAuthmode());
		tableEle.addAttribute(Global.STRUT_KEYFIELDNAME_TAG, table.getKeyfieldname());
		tableEle.addAttribute(Global.STRUT_GROUPKEYFIELD_TAG, table.getGroupkeyfield());
		if (table.getFieldSize() > 0) {
			for (FieldDto field : table.getFields()) {
				Element fieldEle = createStrutFieldEle(field);
				tableEle.add(fieldEle);
			}
		}
		return tableEle;
	}

	public static Element createStrutFieldEle(FieldDto field) {
		Element fieldEle = DocumentHelper.createElement(Global.STRUT_FIELD_TAG);
		fieldEle.addElement(Global.STRUT_FIELDNAME_TAG).setText(field.getField_name());
		fieldEle.addElement(Global.STRUT_FIELDCNAME_TAG).setText(field.getField_desc());
		fieldEle.addElement(Global.STRUT_FIELDTYPE_TAG).setText(field.getType());
		fieldEle.addElement(Global.STRUT_FIELDFORIGNKEY_TAG).setText(field.getForignkey());
		fieldEle.addElement(Global.STRUT_FIELDFIELDTYPE_TAG).setText(field.getField_type());
		return fieldEle;
	}
}
