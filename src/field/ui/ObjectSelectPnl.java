package field.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import field.utils.ComUtils;

public class ObjectSelectPnl<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JDialog dialog = null;
	private JList list;
	private DefaultListModel model;
	private T select;
	private T initValue;
	private String oldMatchTxt = "";
	private boolean matchFlag = true;

	public ObjectSelectPnl(Window owner, String title, Collection<T> all, T value) {
		this.setPreferredSize(new Dimension(308, 493));
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(17);
		JButton button_2 = new JButton("\u641C\u7D22");
		button_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String match = textField.getText();
				if (ComUtils.isStrEmpty(match) || model.getSize() <= 0) {
					return;
				}
				int startIndex = 0;
				if (match.equals(oldMatchTxt)) {
					if (!matchFlag) {
						JOptionPane.showMessageDialog(dialog, "此搜索没记录");
						return;
					}
					int selectIndex = list.getSelectedIndex();
					if (selectIndex < model.getSize() - 1) {
						startIndex = selectIndex + 1;
					}
					matchFlag = true;
				} else {
					matchFlag = true;
					oldMatchTxt = match;
				}

				int seachCount = 0;

				for (; startIndex < model.getSize(); startIndex++) {
					T temp = (T) model.get(startIndex);
					if (temp.toString().indexOf(match) >= 0) {
						list.setSelectedIndex(startIndex);
						break;
					}
					if (startIndex >= model.getSize() - 1) {
						if (seachCount >= model.getSize() - 1) {
							JOptionPane.showMessageDialog(dialog, "没有匹配记录");
							matchFlag = false;
							return;
						} else {
							startIndex = -1;
						}
					}
					seachCount++;
				}

			}
		});
		panel.add(button_2);
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2 || list.getSelectedIndex() < 0) {
					return;
				}
				select = (T) list.getSelectedValue();
				dialog.dispose();
			}
		});
		model = new DefaultListModel();
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel_1, BorderLayout.SOUTH);
		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select = (T) list.getSelectedValue();
				dialog.dispose();
			}
		});
		panel_1.add(button);
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				select = initValue;
				dialog.dispose();
			}
		});
		panel_1.add(button_1);
		dialog = ComUtils.getDialog(owner, title, this);
		init(all, value);
		dialog.setVisible(true);
	}

	private void init(Collection<T> all, T value) {
		initValue = value;
		if (all == null || all.isEmpty()) {
			return;
		}
		for (T item : all) {
			model.addElement(item);
			if (item.equals(value)) {
				list.setSelectedValue(item, true);
			}
		}

	}

	public T getSelect() {
		return select;
	}
}
