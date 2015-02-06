package field.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import field.Global;
import field.dto.FieldDto;
import field.dto.IngoreField;
import field.dto.IngoreTable;
import field.dto.TableDto;
import field.dto.TypeEnum;
import field.utils.ComUtils;
import field.utils.Ele2StringHandler;
import field.utils.FileUtils;

public class DeveloperPnl extends JPanel {
	private static final long serialVersionUID = 1L;
	public static Logger logger = LogManager.getLogger(DeveloperPnl.class.getName());
	private JList list;
	private JList tableList;
	private JEditorPane initData;
	private JEditorPane strut;
	private JEditorPane java;
	private JEditorPane ingore;
	private JEditorPane hbm;
	private JTabbedPane tabbedPane;

	private JEditorPane addEditorTap(String title) {
		JScrollPane scrollPane_1 = new JScrollPane();
		JEditorPane view = new JEditorPane();
		scrollPane_1.setViewportView(view);
		tabbedPane.addTab(title, null, scrollPane_1, null);
		return view;
	}

	public DeveloperPnl(JList list, JList tableList) {
		this.list = list;
		this.tableList = tableList;
		this.setPreferredSize(new Dimension(501, 344));
		setLayout(new BorderLayout(0, 0));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		initData = addEditorTap("initData");
		strut = addEditorTap("strut");
		java = addEditorTap("java");
		hbm = addEditorTap("hbm");
		ingore = addEditorTap("忽略的sturt数据");
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, Global.BOTTOM_HEIGHT));
		add(panel, BorderLayout.SOUTH);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		toolBar.add(panel_1);

		JButton btnNewButton = new JButton("create");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				create();
			}

		});
		panel_1.add(btnNewButton);
	}

	private void create() {
		if (tableList.getSelectedIndex() < 0) {
			return;
		}
		if (list == null || list.getModel().getSize() < 0 || list.getSelectedIndices().length < 0) {
			return;
		}
		List<FieldDto> selects = ComUtils.getListSelectObjs(FieldDto.class, list);
		writeInitData(selects);

		writeStrut(selects);

		writeJava(selects);

		wirteHbm(selects);

	}

	private void wirteHbm(List<FieldDto> selects) {
		hbm.setText("");
		StringBuilder sb = new StringBuilder();
		try {
			if (selects.isEmpty()) {
				selects = ComUtils.getListObjs(FieldDto.class, list.getModel());
				sb.append("<class name=\" \" table=\"").append(((TableDto) tableList.getSelectedValue()).getTable_name())//
						.append("\">").append("\n");
			}
			for (FieldDto obj : selects) {
				String name = obj.getField_name().toLowerCase();
				String prop = ComUtils.createProHbm(name);
				sb.append(prop);
			}
			if (selects.size() == list.getModel().getSize()) {
				sb.append("</class>").append("\n");
			}
			hbm.setText(sb.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeJava(List<FieldDto> selects) {
		java.setText("");
		StringBuilder sb = new StringBuilder();
		try {
			if (selects.isEmpty()) {
				selects = ComUtils.getListObjs(FieldDto.class, list.getModel());
			}
			for (FieldDto obj : selects) {
				String name = obj.getField_name().toLowerCase();
				String desc = obj.getField_desc();
				TypeEnum type = TypeEnum.C;
				if (!obj.getField_type().equalsIgnoreCase("C")) {
					type = TypeEnum.N;
				}
				String prop = ComUtils.createJavaProp(name, desc, type);
				sb.append(prop);
			}
			for (FieldDto obj : selects) {
				String name = obj.getField_name().toLowerCase();
				TypeEnum type = TypeEnum.C;
				if (!obj.getField_type().equalsIgnoreCase("C")) {
					type = TypeEnum.N;
				}
				String getMethod = ComUtils.createJavaGetMethod(name, type);
				sb.append(getMethod);
			}
			for (FieldDto obj : selects) {
				String name = obj.getField_name().toLowerCase();
				TypeEnum type = TypeEnum.C;
				if (!obj.getField_type().equalsIgnoreCase("C")) {
					type = TypeEnum.N;
				}
				String setMethod = ComUtils.createJavaSetMethod(name, type);
				sb.append(setMethod);
			}
			java.setText(sb.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeStrut(List<FieldDto> objs) {
		strut.setText("");
		StringWriter out = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Global.Encoding_Strut);
		Ele2StringHandler handler = new Ele2StringHandler(out, format);
		try {
			if (objs.isEmpty() || list.getModel().getSize() == objs.size()) {
				Element ele = FileUtils.createStrutTableEle((TableDto) tableList.getSelectedValue());
				handler.write(ele);
			} else {
				for (FieldDto obj : objs) {
					Element ele = FileUtils.createStrutFieldEle(obj);
					handler.write(ele);
					handler.println();
				}
			}
			strut.setText(out.toString());
		} finally {
			handler.release();
		}
	}

	private void writeInitData(List<FieldDto> objs) {
		initData.setText("");
		StringWriter out = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Global.Encoding_initData);
		Ele2StringHandler handler = new Ele2StringHandler(out, format);
		try {
			// 如果全选了
			if (objs.size() == 0 || list.getModel().getSize() == objs.size()) {
				Element ele = FileUtils.createInitDataTableEle((TableDto) tableList.getSelectedValue());
				handler.write(ele);
			} else {
				for (FieldDto obj : objs) {
					Element ele = FileUtils.createInitDataFieldEle(obj);
					handler.write(ele);
					handler.println();
				}
			}
			initData.setText(out.toString());
		} finally {
			handler.release();
		}

	}

	public void clearAll() {
		initData.setText("");
		strut.setText("");
		java.setText("");
		hbm.setText("");
	}

	public void init() {
		clearAll();
		initIngore();
	}

	public void initIngore() {
		StringWriter out = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Global.Encoding_Strut);
		Ele2StringHandler handler = new Ele2StringHandler(out, format);
		try {

			if (Global.ingoreTables != null && !Global.ingoreTables.isEmpty()) {
				for (IngoreTable ingoreTable : Global.ingoreTables) {
					if (ingoreTable.getEle() != null) {
						handler.write(ingoreTable.getEle());
						handler.println();
					} else {
						handler.write(ingoreTable.getDesc());
						handler.write("--");
						handler.write(ingoreTable.getName());
						for (IngoreField field : ingoreTable.getFields()) {
							handler.write(field.getEle());
							handler.println();
						}
					}
				}
			}
			ingore.setText(out.toString());
		} finally {
			handler.release();
		}
	}
}
