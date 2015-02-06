package field.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import field.Global;
import field.com.ButtonCellEditor;
import field.com.ButtonCellRender;
import field.dto.FieldDto;
import field.dto.IndexDto;
import field.dto.TableDto;
import field.utils.ComUtils;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class IndexPnl extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private String[] titles = new String[] { "Ãû³Æ", "type", "×Ö¶Î" };
	private DefaultTableModel model;
	private JDialog dialog = null;
	private TableDto tableDto = null;
	private final int indexName = 0;
	private final int indexType = 1;
	private final int indexField = 2;

	/**
	 * Create the panel.
	 */
	public IndexPnl(TableDto curTable) {
		if (curTable == null) {
			return;
		}
		this.setPreferredSize(new Dimension(463, 322));
		setLayout(new BorderLayout(0, 0));
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<IndexDto> indexs = getIndexs();
				tableDto.setIndexs(indexs);
				dialog.dispose();
			}
		});

		JButton button_2 = new JButton("+");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0) {
					model.addRow(new Object[] {});
				} else {
					model.insertRow(index + 1, new Object[] {});
				}

			}
		});
		panel_1.add(button_2);

		JButton btnNewButton = new JButton("-");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0) {
					return;
				}
				model.removeRow(index);
			}
		});
		panel_1.add(btnNewButton);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(30, 0));
		panel_1.add(separator);
		panel_1.add(button);
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		panel_1.add(button_1);
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		table = new JTable();
		model = new DefaultTableModel(titles, 0);
		table.setModel(model);
		scrollPane.setViewportView(table);
		initColumn();
		this.tableDto = curTable;
		initDatas();
		dialog = ComUtils.getDialog(Global.MAIN, "ÉèÖÃË÷Òý", this);
		dialog.setVisible(true);
	}

	private void initDatas() {
		if (this.tableDto.getIndexs() == null || this.tableDto.getIndexs().isEmpty()) {
			return;
		}
		for (IndexDto index : this.tableDto.getIndexs()) {
			model.addRow(new Object[] { index.getName(), index.getType(), index.getFields() });
		}
	}

	private List<IndexDto> getIndexs() {
		List<IndexDto> indexs = new ArrayList<IndexDto>();
		int rowCount = model.getRowCount();
		if (rowCount > 0) {
			for (int i = 0; i < rowCount; i++) {
				String name = (String) table.getValueAt(i, indexName);
				String type = (String) table.getValueAt(i, indexType);
				String fieldStr = (String) table.getValueAt(i, indexField);
				IndexDto index = new IndexDto(name, type, fieldStr);
				indexs.add(index);
			}
		}
		return indexs;
	}

	private void initColumn() {
		TableColumnModel cm = table.getColumnModel();
		TableColumn c1 = cm.getColumn(indexType);
		c1.setPreferredWidth(10);
		c1.setWidth(10);
		TableColumn c2 = cm.getColumn(indexField);
		ButtonCellEditor editor = new ButtonCellEditor();
		editor.addAction(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				List<FieldDto> fields = tableDto.getFields();
				int selectRow = table.getSelectedRow();
				String fieldsStr = (String) table.getValueAt(selectRow, indexField);
				List<FieldDto> values = new ArrayList<FieldDto>();
				if (!ComUtils.isStrEmpty(fieldsStr)) {
					String[] fieldsArr = fieldsStr.split(",");
					for (String temp : fieldsArr) {
						FieldDto field = ComUtils.getObjByProp(FieldDto.class, "field_name", temp, fields, false);
						if (field != null) {
							values.add(field);
						}
					}
				}
				SelectListPnl pnl = new SelectListPnl(dialog, "Ñ¡Ôñ×Ö¶Î", fields, values);
				values = (List<FieldDto>) pnl.getValues();
				if (values == null || values.isEmpty()) {
					table.setValueAt("", selectRow, indexField);
				} else {
					StringBuilder sb = new StringBuilder();
					for (FieldDto field : values) {
						sb.append(field.getField_name()).append(",");
					}
					sb.deleteCharAt(sb.length() - 1);
					table.setValueAt(sb.toString(), selectRow, indexField);
				}
				table.getCellEditor().cancelCellEditing();
			}
		});
		c2.setCellEditor(editor);
		c2.setCellRenderer(new ButtonCellRender());
	}
}
