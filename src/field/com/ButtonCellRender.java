package field.com;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonCellRender implements TableCellRenderer {
	private ButtonEditorCom editorCom = new ButtonEditorCom();

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		editorCom.setValue((String) value);
		return editorCom;
	}

}
