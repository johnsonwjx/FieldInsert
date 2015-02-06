package field.com;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;
	private ButtonEditorCom editorCom = new ButtonEditorCom();

	public Object getCellEditorValue() {
		return editorCom.getValue();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		editorCom.setValue((String) value);
		return editorCom;
	}

	public void addAction(ActionListener action) {
		editorCom.addAction(action);
	}
}