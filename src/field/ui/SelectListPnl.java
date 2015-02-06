package field.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import field.utils.ComUtils;

public class SelectListPnl<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	private JDialog dialog = null;
	private JList list1;
	private DefaultListModel list1Model;
	private JList list2;
	private DefaultListModel list2Model;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane;
	private JTextField textField;
	private Collection<T> initAll;

	private String oldSelectTxt = "";
	private boolean matchFlag = true;
	private Collection<T> values = null;

	public SelectListPnl(Window owner, String title, Collection<T> all, Collection<T> values) {
		this.setPreferredSize(new Dimension(501, 545));
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(this.getPreferredSize().width / 2 - 30, 0));
		panel.add(scrollPane);
		list1 = new JList();
		list1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2) {
					return;
				}
				int index = list1.getSelectedIndex();
				if (index < 0) {
					return;
				}
				Object obj = list1Model.get(index);
				list1Model.removeElementAt(index);
				list2Model.addElement(obj);
				scrollPane_1.getVerticalScrollBar().setValue(list2.getPreferredSize().height);
				list2.setSelectedIndex(list2Model.getSize() - 1);
			}
		});
		list1Model = new DefaultListModel();
		list1.setModel(list1Model);
		scrollPane.setViewportView(list1);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_1.setPreferredSize(new Dimension(50, 100));
		panel_1.setMaximumSize(new Dimension(0, 200));
		panel_1.setLayout(new GridLayout(4, 0, 0, 5));

		JButton button_2 = new JButton("^");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectIndexs = list2.getSelectedIndices();
				if (selectIndexs == null || selectIndexs.length <= 0) {
					return;
				}
				List<Integer> newSelectIndex = new ArrayList<Integer>();
				for (int selectIndex : selectIndexs) {
					if (selectIndex <= 0) {
						newSelectIndex.add(0);
						continue;
					}
					Object obj = list2Model.getElementAt(selectIndex);
					list2Model.remove(selectIndex);
					list2Model.insertElementAt(obj, selectIndex - 1);
					if (newSelectIndex.contains(selectIndex - 1)) {
						newSelectIndex.add(selectIndex);
					} else {
						newSelectIndex.add(selectIndex - 1);
					}
				}
				if (newSelectIndex.size() == 1) {
					Object obj = list2Model.getElementAt(newSelectIndex.get(0));
					list2.setSelectedValue(obj, true);
				} else {
					for (int selectIndex : newSelectIndex) {
						list2.addSelectionInterval(selectIndex, selectIndex);
					}
				}
			}
		});
		panel_1.add(button_2);

		JButton button_3 = new JButton(">");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectIndexs = list1.getSelectedIndices();
				if (selectIndexs == null || selectIndexs.length <= 0) {
					return;
				}
				list2.getSelectionModel().clearSelection();
				for (int index : selectIndexs) {
					Object obj = list1Model.getElementAt(index);
					list2Model.addElement(obj);
				}
				int removeCount = 0;
				for (int index : selectIndexs) {
					list1Model.removeElementAt(index - removeCount);
					removeCount++;
				}
				list2.getSelectionModel().clearSelection();
				list2.setSelectionInterval(list2Model.getSize() - selectIndexs.length, list2Model.getSize() - 1);

			}
		});
		panel_1.add(button_3);

		JButton button_4 = new JButton("<");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectIndexs = list2.getSelectedIndices();
				if (selectIndexs == null || selectIndexs.length <= 0) {
					return;
				}
				list1.getSelectionModel().clearSelection();
				for (int index : selectIndexs) {
					Object obj = list2Model.getElementAt(index);
					list1Model.addElement(obj);
				}

				int removeCount = 0;
				for (int index : selectIndexs) {
					list2Model.removeElementAt(index - removeCount);
					removeCount++;
				}
				list1.getSelectionModel().clearSelection();
				list1.setSelectionInterval(list1Model.getSize() - selectIndexs.length, list1Model.getSize() - 1);
			}
		});
		panel_1.add(button_4);

		JButton btnV = new JButton("V");
		btnV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int[] selectIndexs = list2.getSelectedIndices();
				if (selectIndexs == null || selectIndexs.length <= 0) {
					return;
				}
				List<Integer> newSelectIndex = new ArrayList<Integer>();
				for (int selectIndex : selectIndexs) {
					if (selectIndex >= list2Model.getSize() - 1) {
						newSelectIndex.add(list2Model.getSize() - 1);
						continue;
					}
					T obj = (T) list2Model.getElementAt(selectIndex);
					list2Model.remove(selectIndex);
					list2Model.insertElementAt(obj, selectIndex + 1);
					if (newSelectIndex.contains(selectIndex + 1)) {
						newSelectIndex.add(selectIndex);
					} else {
						newSelectIndex.add(selectIndex + 1);
					}
				}
				if (newSelectIndex.size() == 1) {
					Object obj = list2Model.getElementAt(newSelectIndex.get(0));
					list2.setSelectedValue(obj, true);
				} else {
					for (int selectIndex : newSelectIndex) {
						list2.addSelectionInterval(selectIndex, selectIndex);
					}
				}
			}
		});
		panel_1.add(btnV);
		scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1);

		list2 = new JList();
		list2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2) {
					return;
				}
				int index = list2.getSelectedIndex();
				if (index < 0) {
					return;
				}
				Object obj = list2Model.get(index);
				list2Model.removeElementAt(index);
				list1Model.addElement(obj);
				scrollPane.getVerticalScrollBar().setValue(list1.getPreferredSize().height);
				list1.setSelectedIndex(list1Model.getSize() - 1);
			}
		});
		list2Model = new DefaultListModel();
		list2.setModel(list2Model);
		scrollPane_1.setViewportView(list2);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel_2, BorderLayout.SOUTH);

		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list2Model.isEmpty()) {
					SelectListPnl.this.values = null;
				} else {
					SelectListPnl.this.values.clear();
					for (int i = 0; i < list2Model.getSize(); i++) {
						Object obj = list2Model.getElementAt(i);
						SelectListPnl.this.values.add((T) obj);
					}
				}

				dialog.dispose();
			}
		});
		panel_2.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		panel_2.add(button_1);

		dialog = ComUtils.getDialog(owner, title, this);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_3, BorderLayout.NORTH);

		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(15);

		JButton button_5 = new JButton("\u641C\u7D22");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txt = textField.getText();
				if (ComUtils.isStrEmpty(txt)) {
					return;
				}
				int startIndex = 0;
				if (txt.equals(oldSelectTxt)) {
					if (!matchFlag) {
						JOptionPane.showMessageDialog(dialog, "还是没记录查询");
						return;
					}
					int[] indexs = list1.getSelectedIndices();
					if (indexs.length == 1) {
						startIndex = indexs[0] + 1;
						if (startIndex > list1Model.getSize() - 1) {
							startIndex = 0;
						}
					}
					matchFlag = true;
				} else {
					oldSelectTxt = txt;
					matchFlag = true;
				}
				int searchCount = 0;
				for (; startIndex < list1Model.getSize(); startIndex++) {
					Object temp = list1Model.getElementAt(startIndex);
					if (temp.toString().indexOf(txt) >= 0) {
						list1.setSelectedValue(temp, true);
						return;
					}
					if (startIndex >= list1Model.getSize() - 1) {
						if (searchCount >= list1Model.getSize() - 1) {
							matchFlag = false;
							JOptionPane.showMessageDialog(dialog, "没找到记录");
							return;
						} else {
							startIndex = 0;
						}
					}
					searchCount++;
				}
			}
		});
		panel_3.add(button_5);

		JButton button_6 = new JButton("\u6062\u590D\u88AB\u9009\u9879\u76EE\u521D\u59CB\u987A\u5E8F");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list1Model.isEmpty()) {
					return;
				}

				Iterator<T> iter = initAll.iterator();
				int insertIndex = 0;
				for (; iter.hasNext();) {
					T obj = iter.next();
					Object inserObj = null;
					for (int i = 0; i < list1Model.getSize(); i++) {
						Object temp = list1Model.getElementAt(i);
						if (temp.equals(obj)) {
							inserObj = temp;
							break;
						}
					}
					if (inserObj == null) {
						continue;
					}
					list1Model.removeElement(inserObj);
					list1Model.insertElementAt(inserObj, insertIndex);
					insertIndex++;
					if (insertIndex >= list1Model.getSize() - 1) {
						return;
					}
				}

			}
		});
		panel_3.add(button_6);
		this.initAll = all;
		if (all != null && !all.isEmpty()) {
			initData(values);
		}
		dialog.setVisible(true);
	}

	private void initData(Collection<T> values) {
		this.values = values;
		if (this.values == null) {
			this.values = new ArrayList<T>();
		}
		boolean hasValue = (values != null && !values.isEmpty());
		Iterator<T> iter = initAll.iterator();
		for (; iter.hasNext();) {
			T obj = iter.next();
			if (hasValue && values.contains(obj)) {
				continue;
			}
			list1Model.addElement(obj);
		}
		if (!hasValue) {
			return;
		}
		iter = values.iterator();
		for (; iter.hasNext();) {
			T obj = iter.next();
			list2Model.addElement(obj);
		}
	}

	public Collection<T> getValues() {
		return values;
	}

	public static void main(String[] args) {
		List<Integer> all = new ArrayList<Integer>();
		int i = 150;
		while (i > 0) {
			all.add(i);
			i--;
		}
		List<Integer> values = new ArrayList<Integer>();
		values.add(3);
		new SelectListPnl<Integer>(null, "xx", all, values);
	}
}
