package field.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.table.TableCellEditor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import field.Global;
import field.dto.TypeEnum;

public class ComUtils {
	public static Logger logger = LogManager.getLogger(ComUtils.class.getName());

	public static boolean isStrEmpty(String str) {
		if (str == null) {
			return true;
		}
		return str.length() <= 0;
	}

	public static JDialog getDialog(Window owner, String title, Component com) {
		JDialog dialog = null;
		if (owner == null) {
			dialog = new JDialog(Global.MAIN, title);
		} else {
			if (owner instanceof Frame) {
				dialog = new JDialog((Frame) owner, title);
			} else if (owner instanceof Dialog) {
				dialog = new JDialog((Dialog) owner, title);
			}
		}
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.add(com);
		dialog.pack();
		dialog.setLocationRelativeTo(Global.MAIN);
		return dialog;
	}

	/**
	 * 根据属性名和对应的值 在容器找出改对象 属性值为空 以toString 匹配
	 * 
	 * @param clazz
	 * @param all
	 * @param valueStr
	 * @param propName
	 * @return
	 * @throws Exception
	 */
	public static <T> T getObjInList(Class<T> clazz, Collection<T> all, String valueStr, String propName) throws Exception {
		if (all.isEmpty() || valueStr == null) {
			return null;
		}
		T obj = null;
		Iterator<T> iter = all.iterator();
		for (; iter.hasNext();) {
			T temp = iter.next();
			Object match = null;
			if (propName == null) {
				match = temp.toString();
			} else {
				Field field = clazz.getField(propName);
				field.setAccessible(true);
				match = field.get(temp);
			}
			if (valueStr.equals(match)) {
				obj = temp;
				break;
			}
		}
		return obj;
	}

	class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		JPanel editorCom = null;
		JTextField txt = null;
		JButton btn = null;

		public ButtonCellEditor() {
			txt = new JTextField();
			btn = new JButton("...");
			editorCom = new JPanel(new BorderLayout());
			editorCom.add(txt, BorderLayout.CENTER);
			editorCom.add(btn, BorderLayout.EAST);
		}

		public Object getCellEditorValue() {
			return txt.getText();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return editorCom;
		}

		public void addAction(ActionListener action) {
			btn.addActionListener(action);
		}
	}

	public static <T> T getObjByProp(Class<T> clazz, String propName, String value, Collection<T> all, boolean ingoreCase) {
		T obj = null;
		try {
			if (all.isEmpty() || isStrEmpty(value) || isStrEmpty(propName)) {
				return null;
			}
			Field field = clazz.getDeclaredField(propName);
			field.setAccessible(true);
			for (T temp : all) {
				Object tempValue = field.get(temp);
				if (ingoreCase) {
					if (value.equalsIgnoreCase(tempValue.toString())) {
						obj = temp;
						break;
					}
				} else {
					if (value.equals(tempValue)) {
						obj = temp;
						break;
					}
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}

	public static <T> void resortNum(Class<T> clazz, String sortProName, DefaultListModel model) {
		try {
			if (model.isEmpty()) {
				return;
			}
			Field field = clazz.getDeclaredField(sortProName);
			field.setAccessible(true);
			for (int i = 0; i < model.getSize(); i++) {
				Object obj = model.getElementAt(i);
				field.setInt(obj, i + 1);
			}
		} catch (Exception e) {
			logger.debug(sortProName);
			logger.error(e.getMessage(), e);
		}
	}

	public static <T> void updateTxtUIByPropMap(Class<T> clazz, T obj, Map<String, JTextField> propMaps) {
		try {
			for (String key : propMaps.keySet()) {
				JTextField com = propMaps.get(key);
				String value = "";
				if (obj != null) {
					Field field = clazz.getDeclaredField(key);
					field.setAccessible(true);
					Object tempValue = field.get(obj);
					if (tempValue != null) {
						value = (String) tempValue;
					}
				}
				com.setText(value);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public static <T> List<T> getListObjs(Class<T> clazz, ListModel model) {
		List<T> list = new ArrayList<T>();
		try {
			if (model.getSize() > 0) {
				for (int i = 0; i < model.getSize(); i++) {
					list.add((T) model.getElementAt(i));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	public static <T> List<T> getListSelectObjs(Class<T> clazz, JList list) {
		List<T> result = new ArrayList<T>();
		Object[] values = list.getSelectedValues();
		try {
			if (values.length > 0) {
				for (Object value : values) {
					result.add((T) value);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	public static <T> void updatePropByUItxtMap(Class<T> clazz, T obj, Map<String, JTextField> propMaps) {
		try {
			for (String key : propMaps.keySet()) {
				JTextField com = propMaps.get(key);
				String value = "";
				if (obj != null && !ComUtils.isStrEmpty(com.getText())) {
					value = com.getText();
				}
				Field field = clazz.getDeclaredField(key);
				field.setAccessible(true);
				field.set(obj, value);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public static String coverNULL(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	public static String createJavaProp(String name, String desc, TypeEnum type) {
		if (isStrEmpty(name) || type == null) {
			return null;
		}
		if (desc == null) {
			desc = "";
		}
		StringBuilder sb = new StringBuilder();
		if (!desc.isEmpty()) {
			sb.append("\t//").append(desc).append("\n");
		}
		sb.append("\t").append("private").append(" ").append(type.getJavaType()).append(" ").append(name).append(";").append("\n");
		return sb.toString();
	}

	public static String getMethodName(String name) {
		if (isStrEmpty(name)) {
			return null;
		}
		name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase();
		return name;
	}

	public static String createJavaGetMethod(String name, TypeEnum type) {
		if (isStrEmpty(name) || type == null) {
			return null;
		}
		String methodName = getMethodName(name);
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("public").append(" ").append(type.getJavaType()).append(" get").append(methodName).append("()").append(" {\n");
		sb.append("\t\t").append("return ").append(name).append(";").append("\n");
		sb.append("\t").append("}").append("\n");
		return sb.toString();
	}

	public static String createJavaSetMethod(String name, TypeEnum type) {
		if (isStrEmpty(name) || type == null) {
			return null;
		}
		String methodName = getMethodName(name);
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("public").append(" void ").append(type.getJavaType()).append(" set")//
				.append(methodName).append("(").append(type.getJavaType()).append(" ")//
				.append(name).append(")").append(" {\n");
		sb.append("\t\t").append("this.").append(name).append("=").append(name).append(";").append("\n");
		sb.append("\t").append("}").append("\n");
		return sb.toString();
	}

	public static String createProHbm(String name) {
		if (isStrEmpty(name)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if ("id".equalsIgnoreCase(name)) {
			sb.append("\t").append("<id name=\"id\" type=\"string\">\n")//
					.append("\t\t<column name=\"ID\" />\n")//
					.append("\t\t<generator class=\"uuid.hex\" />\n")//
					.append("\t</id>\n");
		} else {
			sb.append("\t").append("<property column=\"")//
					.append(name.toUpperCase()).append("\" name=\"")//
					.append(name).append("\"").append(" />").append("\n");
		}
		return sb.toString();
	}

	public static int seachList(JList list, String match, int startIndex, boolean ingoreCase) {
		if (list == null || isStrEmpty(match) || list.getModel().getSize() <= 0 || startIndex < 0 || startIndex >= list.getModel().getSize()) {
			return -1;
		}
		ListModel model = list.getModel();
		int searchCount = 0;
		for (; startIndex < model.getSize(); startIndex++) {
			if (ingoreCase) {
				if (model.getElementAt(startIndex).toString().toUpperCase().indexOf(match.toUpperCase()) >= 0) {
					return startIndex;
				}
			} else {
				if (model.getElementAt(startIndex).toString().indexOf(match) >= 0) {
					return startIndex;
				}
			}

			searchCount++;
			if (searchCount >= model.getSize()) {
				return -1;
			}
			if (startIndex >= model.getSize() - 1) {
				startIndex = -1;
			}
		}
		return -1;
	}

	public static int getButtonGroupSelectIndex(ButtonGroup bg) {
		if (bg.getButtonCount() <= 0) {
			return -1;
		}
		Enumeration<AbstractButton> bEnum = bg.getElements();
		for (int i = 0; bEnum.hasMoreElements(); i++) {
			AbstractButton temp = bEnum.nextElement();
			if (temp.isSelected()) {
				return i;
			}

		}
		return -1;
	}

	public static <T> DefaultListModel getModelBySort(Class<T> clazz, String propName, ListModel model, Collection<String> sortValues) {
		try {
			if (isStrEmpty(propName) || model.getSize() <= 0 || sortValues.isEmpty()) {
				return null;
			}
			logger.debug("数量比:" + model.getSize() + ":" + sortValues.size());
			List<T> all = getListObjs(clazz, model);
			Field field = clazz.getDeclaredField(propName);
			field.setAccessible(true);
			DefaultListModel result = new DefaultListModel();
			List<Integer> inserteds = new ArrayList<Integer>();
			for (String v : sortValues) {
				T obj = getObjByProp(clazz, propName, v, all, true);
				if (obj == null) {
					logger.debug("忽略了:" + v);
					continue;
				}
				// 已经插入
				result.addElement(obj);
				inserteds.add(all.indexOf(obj));
			}
			// 为插入,继续插入
			for (int i = 0; i < all.size(); i++) {
				if (inserteds.contains(i)) {
					continue;
				}
				result.addElement(all.get(i));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
