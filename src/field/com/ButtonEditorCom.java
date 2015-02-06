package field.com;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ButtonEditorCom extends JPanel {
	private static final long serialVersionUID = 1L;
	JTextField txt = null;
	JButton btn = null;

	public ButtonEditorCom() {
		this.setLayout(new BorderLayout(-1, -1));
		txt = new JTextField();
		btn = new JButton("...");
		btn.setPreferredSize(new Dimension(25, 0));
		this.add(txt, BorderLayout.CENTER);
		this.add(btn, BorderLayout.EAST);
	}

	public String getValue() {
		return txt.getText();
	}

	public void addAction(ActionListener action) {
		this.btn.addActionListener(action);
	}

	public void setValue(String value) {
		txt.setText(value);
	}
}
