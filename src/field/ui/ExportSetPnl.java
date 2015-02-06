package field.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListModel;

import field.Global;
import field.utils.ComUtils;

public class ExportSetPnl extends JPanel {
	private static final long serialVersionUID = 1L;
	private ButtonGroup bg;
	private int countFlag = -1;
	private JDialog dialog;
	private boolean result;

	private int sortFlag = -1;
	private ButtonGroup bg1;

	public ExportSetPnl(ListModel model) {
		this.setPreferredSize(new Dimension(270, 134));
		this.dialog = ComUtils.getDialog(Global.MAIN, "导出设置", this);
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);

		JLabel label = new JLabel("\u987A\u5E8F\uFF1A");
		panel_2.add(label);
		JRadioButton radioButton_2 = new JRadioButton(" \u5F53\u524D");
		radioButton_2.setSelected(true);
		panel_2.add(radioButton_2);

		JRadioButton radioButton = new JRadioButton("\u5EFA\u8868");
		panel_2.add(radioButton);

		JRadioButton rdbtnStrut = new JRadioButton("struts");
		panel_2.add(rdbtnStrut);
		bg = new ButtonGroup();
		for (Component c : panel_2.getComponents()) {
			if (c instanceof JRadioButton) {
				bg.add((AbstractButton) c);
			}
		}
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);

		JLabel label_1 = new JLabel("\u8FC7\u6EE4:");
		panel_3.add(label_1);

		JRadioButton radioButton_3 = new JRadioButton("\u4E0D ");
		radioButton_3.setSelected(true);
		panel_3.add(radioButton_3);

		JRadioButton rdbtnStruts = new JRadioButton("sturts\u6CA1\u6709");
		panel_3.add(rdbtnStruts);
		bg1 = new ButtonGroup();
		for (Component c : panel_3.getComponents()) {
			if (c instanceof JRadioButton) {
				bg1.add((AbstractButton) c);
			}
		}
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);

		StringBuilder sb = new StringBuilder();

		setTitle(model, sb);

		JLabel top = new JLabel(sb.toString());
		panel_1.add(top);

		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.SOUTH);

		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sortFlag = ComUtils.getButtonGroupSelectIndex(bg);
				countFlag = ComUtils.getButtonGroupSelectIndex(bg1);
				result = true;
				dialog.dispose();
			}
		});
		panel_4.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = false;
				dialog.dispose();
			}
		});
		panel_4.add(button_1);
		dialog.setVisible(true);
	}

	private void setTitle(ListModel model, StringBuilder sb) {
		sb.append("数量: 当前:").append(model.getSize()).append("; 初始建表:").append(Global.innitSort.size())//
				.append("; 初始strut").append(Global.strutSort.size());

	}

	public int getCountFlag() {
		return countFlag;
	}

	public boolean getResult() {
		return this.result;
	}

	public int getSortFlag() {
		return sortFlag;
	}
}
