package field;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import field.dto.FieldDto;
import field.dto.TableDto;
import field.ui.DeveloperPnl;
import field.ui.ExportSetPnl;
import field.ui.IndexPnl;
import field.ui.ObjectSelectPnl;
import field.ui.SelectListPnl;
import field.utils.ComUtils;
import field.utils.FileUtils;

public class FieldMain extends JFrame {
	public static Logger logger = LogManager.getLogger(ComUtils.class.getName());
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList list;
	private DefaultListModel model;
	private boolean initData = false;
	private TableDto curTableDto = null;
	private FieldDto curField = null;
	private int index_tableLastSelect = -1;
	private int index_fieldLastSelect = -1;
	private JList list_1;
	private DefaultListModel fieldModel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JButton btnNewButton;
	private JTextField textField_15;
	private JTextField textField_16;
	private final Map<String, JTextField> tablePropMaps = new HashMap<String, JTextField>();
	private final Map<String, JTextField> fieldPropMaps = new HashMap<String, JTextField>();
	private JPanel panel_4;
	private boolean develop = false;
	private JSplitPane splitPane;
	private JPanel panel_5;
	private JSplitPane splitPane_1;
	private JPanel panel_8;
	private JPanel panel_7;
	private DeveloperPnl developPnl;
	private JSpinner spinner;
	private static final int DEVELOP_HEIGHT = 100;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Global.clear();
					FieldMain frame = new FieldMain();
					Global.MAIN = frame;
					frame.setVisible(true);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FieldMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(960, 712));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		model = new DefaultListModel();
		fieldModel = new DefaultListModel();

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		panel.add(toolBar, BorderLayout.NORTH);
		JButton btnxml_2 = new JButton("\u8BFB\u5165\u5EFA\u8868XML");
		btnxml_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				initData = true;
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
					int returnValue = chooser.showOpenDialog(FieldMain.this);
					if (returnValue != JFileChooser.APPROVE_OPTION) {
						return;
					}
					File file = chooser.getSelectedFile();
					if (file != null) {
						SAXReader reader = new SAXReader();
						Document doc = reader.read(file);
						Global.Encoding_initData = doc.getXMLEncoding();
						Global.Encoding_java = Global.Encoding_initData;
						Global.Encoding_Strut = Global.Encoding_initData;

						List<Element> tableEles = doc.selectNodes("//*[" + Global.cover_node_lowercase(Global.TABLE_TAG) + "]");
						if (!tableEles.isEmpty()) {
							List<TableDto> tableDtos = new ArrayList<TableDto>();
							for (Element tableEle : tableEles) {
								TableDto table = FileUtils.element2TableDto(tableEle, tableDtos.size() + 1);
								tableDtos.add(table);
							}
							int flag = JOptionPane.OK_OPTION;
							if (!model.isEmpty()) {
								flag = JOptionPane.showConfirmDialog(Global.MAIN, "确认导入吗?", "", JOptionPane.OK_CANCEL_OPTION);
							}
							if (JOptionPane.OK_OPTION == flag) {
								initData = true;
								init();
								Global.innitSort.clear();
								for (TableDto table : tableDtos) {
									Global.innitSort.add(table.getTable_name());
									model.addElement(table);
								}
							}
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Global.MAIN, "导入错误");
					logger.error(e.getMessage(), e);
				} finally {
					initData = false;
				}
			}

		});
		toolBar.add(btnxml_2);

		JButton btnxml_1 = new JButton("\u8BFB\u5165StrutXML");
		btnxml_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (model.getSize() <= 0) {
						JOptionPane.showMessageDialog(Global.MAIN, "请先导入建表XML");
						return;
					}
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
					int returnFlag = chooser.showOpenDialog(FieldMain.this);
					if (JFileChooser.APPROVE_OPTION != returnFlag) {
						return;
					}
					File file = chooser.getSelectedFile();
					if (file == null) {
						return;
					}
					SAXReader reader = new SAXReader();
					Document doc = reader.read(file);
					Global.Encoding_Strut = doc.getXMLEncoding();
					List<Element> tableEles = doc.selectNodes("//*[" + Global.cover_node_lowercase(Global.STRUT_TABLE_TAG) + "]");
					if (tableEles.isEmpty()) {
						return;
					}
					if (model.isEmpty()) {
						return;
					}
					// 顺序不同时，改来改去
					List<TableDto> tableDtos = new LinkedList<TableDto>();
					for (int i = 0; i < model.getSize(); i++) {
						Object obj = model.getElementAt(i);
						tableDtos.add((TableDto) obj);
					}
					List<String> strutSort = FileUtils.updatePropByStrutElement(tableDtos, tableEles);
					int flag = JOptionPane.showConfirmDialog(Global.MAIN, "确认导入吗?", "", JOptionPane.OK_CANCEL_OPTION);
					if (JOptionPane.OK_OPTION == flag) {
						initData = true;
						init();
						Global.strutSort.clear();
						Global.strutSort = strutSort;
						for (TableDto table : tableDtos) {
							model.addElement(table);
						}
						ComUtils.resortNum(TableDto.class, "sortnum", model);
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(Global.MAIN, "读入错误");
					logger.error(e2.getMessage(), e2);
				} finally {
					initData = false;
				}
			}
		});
		toolBar.add(btnxml_1);

		JButton button_12 = new JButton("\u5BFC\u51FA\u5EFA\u8868XML");
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultListModel exprotModel = model;
				if (!Global.strutSort.isEmpty() && !Global.innitSort.isEmpty()) {
					ExportSetPnl pnl = new ExportSetPnl(model);
					if (!pnl.getResult()) {
						return;
					}
					int sortFlag = pnl.getSortFlag();
					switch (sortFlag) {
					case 1:
						exprotModel = ComUtils.getModelBySort(TableDto.class, "table_name", model, Global.innitSort);
						break;
					case 2:
						exprotModel = ComUtils.getModelBySort(TableDto.class, "table_name", model, Global.strutSort);
						break;
					default:
						break;
					}
				}
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
					chooser.setSelectedFile(new File("InitData.xml"));
					chooser.showSaveDialog(FieldMain.this);
					File file = chooser.getSelectedFile();
					if (file == null) {
						return;
					}
					file.deleteOnExit();
					FileUtils.writeInnitDataXML(exprotModel, file);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Global.MAIN, "保存错误");
					logger.error(e.getMessage(), e);
				}

			}
		});
		toolBar.add(button_12);

		JButton btnxml = new JButton("\u5BFC\u51FAStrutXML");
		btnxml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel exprotModel = model;
				if (!Global.strutSort.isEmpty() && !Global.innitSort.isEmpty()) {
					ExportSetPnl pnl = new ExportSetPnl(model);
					if (!pnl.getResult()) {
						return;
					}
					int sortFlag = pnl.getSortFlag();
					switch (sortFlag) {
					case 1:
						exprotModel = ComUtils.getModelBySort(TableDto.class, "table_name", model, Global.innitSort);
						break;
					case 2:
						exprotModel = ComUtils.getModelBySort(TableDto.class, "table_name", model, Global.strutSort);
						break;
					default:
						break;
					}
				}
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
					chooser.setSelectedFile(new File("Strut.xml"));
					chooser.showSaveDialog(FieldMain.this);
					File file = chooser.getSelectedFile();
					if (file == null) {
						return;
					}
					file.deleteOnExit();
					FileUtils.writeStrutXml(exprotModel, file);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(Global.MAIN, "保存错误");
					logger.error(e1.getMessage(), e1);
				}

			}
		});
		toolBar.add(btnxml);

		final JToggleButton toggleButton = new JToggleButton("\u5F00\u53D1\u4EBA\u5458\u5DE5\u5177");
		JPanel panel_3;
		toggleButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				develop = toggleButton.isSelected();
				if (develop) {
					list_1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					splitPane.remove(panel_5);
					developPnl.init();
					splitPane_1.setBottomComponent(developPnl);
					splitPane_1.setDividerLocation(splitPane_1.getPreferredSize().height - DEVELOP_HEIGHT);
				} else {
					list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					splitPane.add(panel_5);
					splitPane_1.setBottomComponent(null);
				}
				panel_5.setVisible(!develop);
				panel_4.setVisible(!develop);
				panel_7.setVisible(!develop);
				panel_8.setVisible(!develop);
			}
		});

		JButton button_7 = new JButton("\u6062\u590D\u5220\u9664\u8868");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectListPnl<TableDto> pnl = new SelectListPnl<TableDto>(Global.MAIN, "恢复删除表格", Global.removes, null);
				if (pnl.getValues() != null) {
					for (TableDto obj : pnl.getValues()) {
						model.addElement(obj);
						Global.removes.remove(obj);
					}
					ComUtils.resortNum(TableDto.class, "sortnum", model);
				}
			}
		});
		toolBar.add(button_7);
		toolBar.add(toggleButton);

		JButton button_14 = new JButton("\u4FDD\u5B58\u8868\u683C\u5F53\u524D\u4FEE\u6539");
		button_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTable();
			}

		});
		toolBar.add(button_14);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar_1 = new JToolBar();
		panel_3.add(toolBar_1, BorderLayout.SOUTH);
		toolBar_1.setFloatable(false);
		panel_7 = new JPanel();
		toolBar_1.add(panel_7);
		JButton button_3 = new JButton("\u589E\u52A0");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curTableDto == null) {
					return;
				}
				int index = list_1.getSelectedIndex();
				FieldDto obj = new FieldDto();
				if (index < 0) {
					curTableDto.addField(obj);
					fieldModel.addElement(obj);
				} else {
					curTableDto.addField(index + 1, obj);
					fieldModel.insertElementAt(obj, index + 1);
				}
				ComUtils.resortNum(FieldDto.class, "sortnum", fieldModel);
			}
		});

		panel_7.add(button_3);

		JButton button_4 = new JButton("\u5220\u9664");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list_1.getSelectedIndex();
				if (index < 0) {
					return;
				}
				curTableDto.removeField(index);
				fieldModel.removeElementAt(index);
				if (fieldModel.isEmpty()) {
					return;
				}
				if (index > fieldModel.size() - 1) {
					index = fieldModel.size() - 1;
				}
				ComUtils.resortNum(FieldDto.class, "sortnum", fieldModel);
				list_1.setSelectedValue(fieldModel.get(index), true);
			}
		});

		panel_7.add(button_4);
		JButton button_1 = new JButton("\u4E0A\u79FB");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list_1.getSelectedIndex();
				if (index <= 0) {
					return;
				}
				Object obj = fieldModel.get(index);
				fieldModel.removeElementAt(index);
				fieldModel.insertElementAt(obj, --index);
				list_1.setSelectedValue(obj, true);
				ComUtils.resortNum(FieldDto.class, "sortnum", fieldModel);
			}
		});
		panel_7.add(button_1);

		JButton button_2 = new JButton("\u4E0B\u79FB");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list_1.getSelectedIndex();
				if (index >= fieldModel.size() - 1) {
					return;
				}
				Object obj = fieldModel.get(index);
				fieldModel.removeElementAt(index);
				fieldModel.insertElementAt(obj, ++index);
				list_1.setSelectedValue(obj, true);
				ComUtils.resortNum(FieldDto.class, "sortnum", fieldModel);
			}
		});
		panel_7.add(button_2);

		JButton button = new JButton("\u6E05\u7A7A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int flag = JOptionPane.showConfirmDialog(Global.MAIN, "确认清空吗?", "", JOptionPane.OK_CANCEL_OPTION);
				if (JOptionPane.OK_OPTION == flag) {
					fieldModel.clear();
				}
			}
		});
		panel_7.add(button);
		spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(60, 20));
		panel_7.add(spinner);

		JButton button_13 = new JButton("\u8BBE\u7F6E\u987A\u5E8F");
		button_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert2fields();
			}

		});
		panel_7.add(button_13);

		textField = new JTextField();
		toolBar_1.add(textField);
		textField.setColumns(10);

		JButton button_8 = new JButton("\u641C\u7D22");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newTxt = textField.getText();
				int index = list_1.getSelectedIndex();
				index++;
				if (index >= fieldModel.getSize()) {
					index = 0;
				}
				index = ComUtils.seachList(list_1, newTxt, index, true);
				if (index != -1) {
					list_1.setSelectedValue(fieldModel.getElementAt(index), true);
				}
			}
		});
		toolBar_1.add(button_8);

		panel_4 = new JPanel();
		panel_4.setPreferredSize(new Dimension(0, 120));
		panel_3.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(null);

		textField_1 = new JTextField();
		textField_1.setBounds(48, 6, 246, 28);
		panel_4.add(textField_1);
		textField_1.setColumns(10);

		JLabel label = new JLabel("\u8868\u63CF\u8FF0");
		label.setBounds(6, 12, 61, 16);
		panel_4.add(label);

		textField_2 = new JTextField();
		textField_2.setBounds(340, 4, 241, 28);
		panel_4.add(textField_2);
		textField_2.setColumns(10);

		JLabel label_1 = new JLabel("\u8868\u540D");
		label_1.setBounds(306, 10, 36, 16);
		panel_4.add(label_1);

		btnNewButton = new JButton("\u7D22\u5F15");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (curTableDto != null) {
					new IndexPnl(curTableDto);
					setIndexBtn(curTableDto);
				}

			}
		});
		btnNewButton.setBounds(26, 46, 71, 29);
		panel_4.add(btnNewButton);

		textField_10 = new JTextField();
		textField_10.setBounds(239, 45, 126, 28);
		panel_4.add(textField_10);
		textField_10.setColumns(10);

		textField_11 = new JTextField();
		textField_11.setBounds(450, 45, 36, 28);
		panel_4.add(textField_11);
		textField_11.setColumns(10);

		textField_12 = new JTextField();
		textField_12.setBounds(596, 45, 36, 28);
		panel_4.add(textField_12);
		textField_12.setColumns(10);

		JLabel lblMode = new JLabel("mode");
		lblMode.setBounds(416, 51, 36, 16);
		panel_4.add(lblMode);

		JLabel lblAuthmode = new JLabel("authmode");
		lblAuthmode.setBounds(527, 51, 71, 16);
		panel_4.add(lblAuthmode);

		JButton btnKeyfieldname = new JButton("keyfieldname");
		btnKeyfieldname.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (curTableDto == null) {
					return;
				}
				Collection<FieldDto> all = curTableDto.getFields();
				String txt = textField_13.getText();
				Collection<FieldDto> values = new ArrayList<FieldDto>();
				if (!ComUtils.isStrEmpty(txt)) {
					String[] fieldArr = txt.toUpperCase().split(",");
					for (String fieldStr : fieldArr) {
						FieldDto obj = ComUtils.getObjByProp(FieldDto.class, "field_name", fieldStr, all, false);
						if (obj != null) {
							values.add(obj);
						}
					}
				}
				SelectListPnl<FieldDto> pnl = new SelectListPnl<FieldDto>(Global.MAIN, "keyfield选择", all, values);
				if (pnl.getValues() != null) {
					StringBuilder sb = new StringBuilder();
					for (FieldDto field : pnl.getValues()) {
						sb.append(field.getField_name()).append(",");
					}
					sb.deleteCharAt(sb.length() - 1);
					curTableDto.setKeyfieldname(sb.toString());
				} else {
					curTableDto.setKeyfieldname("");
				}
				updateUIBycurTable(curTableDto);
			}
		});
		btnKeyfieldname.setBounds(203, 85, 117, 29);
		panel_4.add(btnKeyfieldname);

		JButton btnGroupkeyfield = new JButton("groupkeyfield");
		btnGroupkeyfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (curTableDto == null) {
					return;
				}
				Collection<FieldDto> all = curTableDto.getFields();
				String txt = textField_14.getText();
				Collection<FieldDto> values = new ArrayList<FieldDto>();
				if (!ComUtils.isStrEmpty(txt)) {
					String[] fieldArr = txt.toUpperCase().split(",");
					for (String fieldStr : fieldArr) {
						FieldDto obj = ComUtils.getObjByProp(FieldDto.class, "field_name", fieldStr, all, false);
						if (obj != null) {
							values.add(obj);
						}
					}
				}
				SelectListPnl<FieldDto> pnl = new SelectListPnl<FieldDto>(Global.MAIN, "groupfield选择", all, values);
				if (pnl.getValues() != null) {
					StringBuilder sb = new StringBuilder();
					for (FieldDto field : pnl.getValues()) {
						sb.append(field.getField_name()).append(",");
					}
					sb.deleteCharAt(sb.length() - 1);
					curTableDto.setGroupkeyfield(sb.toString());
				} else {
					curTableDto.setGroupkeyfield("");
				}
				updateUIBycurTable(curTableDto);
			}
		});
		btnGroupkeyfield.setBounds(515, 85, 117, 29);
		panel_4.add(btnGroupkeyfield);

		JLabel lblFathertablename = new JLabel("fathertablename");
		lblFathertablename.setBounds(136, 51, 102, 16);
		panel_4.add(lblFathertablename);

		textField_13 = new JTextField();
		textField_13.setBounds(6, 84, 195, 28);
		panel_4.add(textField_13);
		textField_13.setColumns(10);

		textField_14 = new JTextField();
		textField_14.setBounds(330, 84, 184, 28);
		panel_4.add(textField_14);
		textField_14.setColumns(10);
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.4);
		panel_1.add(splitPane, BorderLayout.CENTER);
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);
		list_1 = new JList();
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent event) {
				if (develop) {
					return;
				}
				if (event.getValueIsAdjusting()) {
					fieldSelectChangeUpdate();
				} else {
					fieldSelectCahngeUIUpdate();
				}
			}
		});
		list_1.setModel(fieldModel);
		scrollPane_1.setViewportView(list_1);

		panel_5 = new JPanel();
		splitPane.setRightComponent(panel_5);
		panel_5.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_5.setLayout(new GridLayout(18, 0, 0, 0));

		JLabel label_2 = new JLabel("\u5B57\u6BB5\u540D");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_2);

		textField_4 = new JTextField();
		panel_5.add(textField_4);
		textField_4.setColumns(10);

		JLabel label_3 = new JLabel("\u5B57\u6BB5\u63CF\u8FF0");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_3);

		textField_5 = new JTextField();
		panel_5.add(textField_5);
		textField_5.setColumns(10);

		JLabel label_4 = new JLabel("\u5B57\u6BB5\u7C7B\u578B");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_4);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_5.add(panel_6);

		radioButton_1 = new JRadioButton("\u5B57\u7B26");
		panel_6.add(radioButton_1);

		radioButton = new JRadioButton("\u6570\u5B57");
		panel_6.add(radioButton);
		ButtonGroup bg = new ButtonGroup();
		for (Component c : panel_6.getComponents()) {
			bg.add((AbstractButton) c);
		}

		JLabel label_5 = new JLabel("\u957F\u5EA6");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_5);

		textField_6 = new JTextField();
		panel_5.add(textField_6);
		textField_6.setColumns(10);

		JLabel label_8 = new JLabel("\u9ED8\u8BA4\u503C");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_8);

		textField_9 = new JTextField();
		panel_5.add(textField_9);
		textField_9.setColumns(10);

		JLabel label_6 = new JLabel("\u7CBE\u786E\u5EA6");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_6);

		textField_7 = new JTextField();
		panel_5.add(textField_7);
		textField_7.setColumns(10);

		JLabel label_7 = new JLabel("\u662F\u5426\u7A7A");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(label_7);

		textField_8 = new JTextField();
		panel_5.add(textField_8);
		textField_8.setColumns(10);

		JLabel lblTypestrut = new JLabel("type(strut)");
		lblTypestrut.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(lblTypestrut);

		textField_15 = new JTextField();
		panel_5.add(textField_15);
		textField_15.setColumns(10);

		JLabel lblForigkeystrut = new JLabel("forigkey(strut)");
		lblForigkeystrut.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(lblForigkeystrut);

		textField_16 = new JTextField();
		textField_16.setLayout(new BorderLayout());
		JButton btn = new JButton("...");
		textField_16.add(btn, BorderLayout.EAST);
		btn.setPreferredSize(new Dimension(80, 0));
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (curTableDto == null) {
					return;
				}
				FieldDto value = null;
				String txt = textField_16.getText();
				Collection<FieldDto> all = curTableDto.getFields();
				if (!ComUtils.isStrEmpty(txt)) {
					value = ComUtils.getObjByProp(FieldDto.class, "field_name", txt, all, false);
				}
				ObjectSelectPnl<FieldDto> pnl = new ObjectSelectPnl<FieldDto>(Global.MAIN, "选择", all, value);
				if (pnl.getSelect() == null) {
					textField_16.setText("");
				} else {
					textField_16.setText(pnl.getSelect().getField_name());
				}
			}
		});
		panel_5.add(textField_16);
		textField_16.setColumns(10);
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane);
		panel_2.setPreferredSize(new Dimension(300, 0));
		list = new JList();
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evn) {
				try {
					if (initData) {
						return;
					}
					if (evn.getValueIsAdjusting()) {
						if (index_tableLastSelect < 0) {
							return;
						}
						fieldSelectChangeUpdate();
						list_1.getSelectionModel().clearSelection();
						updateTableProByUI(curTableDto);
						if (!isDataChange()) {
							curTableDto = null;
							return;
						}
						int flag = JOptionPane.showConfirmDialog(Global.MAIN, "数据以改变，确认修改吗?", "", JOptionPane.YES_NO_OPTION);
						if (flag != JOptionPane.OK_OPTION) {
							curTableDto = null;
							return;
						}
						model.setElementAt(curTableDto, index_tableLastSelect);
						curTableDto = null;
					} else {
						index_tableLastSelect = list.getSelectedIndex();
						if (index_tableLastSelect < 0 || index_tableLastSelect > model.getSize()) {
							index_tableLastSelect = -1;
						} else {
							TableDto table = (TableDto) model.get(index_tableLastSelect);
							curTableDto = table.clone();
						}
						updateUIBycurTable(curTableDto);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

			}
		});

		scrollPane.setViewportView(list);

		JToolBar toolBar_2 = new JToolBar();
		toolBar_2.setFloatable(false);
		panel_2.add(toolBar_2, BorderLayout.NORTH);
		panel_8 = new JPanel();
		toolBar_2.add(panel_8);
		JButton button_5 = new JButton("\u6DFB\u52A0");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = list.getSelectedIndex();
				model.insertElementAt(new TableDto(), index + 1);
				ComUtils.resortNum(TableDto.class, "sortnum", model);
			}
		});

		panel_8.add(button_5);

		JButton button_6 = new JButton("\u5220\u9664");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if (index < 0) {
					return;
				}

				Global.removes.add((TableDto) model.getElementAt(index));
				model.removeElementAt(index);
				if (model.isEmpty()) {
					return;
				}
				if (index > model.getSize() - 1) {
					index = model.getSize() - 1;
				}
				ComUtils.resortNum(TableDto.class, "sortnum", model);
				list.setSelectedValue(model.getElementAt(index), true);
			}
		});
		panel_8.add(button_6);

		JButton button_9 = new JButton("\u4E0A\u79FB");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if (index <= 0) {
					return;
				}
				Object obj = model.getElementAt(index);
				model.removeElementAt(index);
				model.insertElementAt(obj, --index);
				list.setSelectedValue(obj, true);
				ComUtils.resortNum(TableDto.class, "sortnum", model);
			}
		});
		panel_8.add(button_9);

		JButton button_10 = new JButton("\u4E0B\u79FB");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if (index >= model.size() - 1) {
					return;
				}
				Object obj = model.getElementAt(index);
				model.removeElementAt(index);
				model.insertElementAt(obj, ++index);
				list.setSelectedValue(obj, true);
				ComUtils.resortNum(TableDto.class, "sortnum", model);
			}
		});
		panel_8.add(button_10);

		textField_3 = new JTextField();
		toolBar_2.add(textField_3);
		textField_3.setColumns(10);

		JButton button_11 = new JButton("\u641C\u7D22");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newTxt = textField_3.getText();
				int index = list.getSelectedIndex();
				index++;
				if (index >= model.getSize()) {
					index = 0;
				}
				index = ComUtils.seachList(list, newTxt, index, true);
				if (index != -1) {
					list.setSelectedValue(model.getElementAt(index), true);
				}
			}
		});
		toolBar_2.add(button_11);
		splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(1.0);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setTopComponent(panel);
		splitPane_1.setBottomComponent(null);
		contentPane.add(splitPane_1, BorderLayout.CENTER);
		developPnl = new DeveloperPnl(list_1, list);
		developPnl.setPreferredSize(new Dimension(0, DEVELOP_HEIGHT));
		initRelation();
	}

	private void initRelation() {
		tablePropMaps.put("table_desc", textField_1);
		tablePropMaps.put("table_name", textField_2);
		tablePropMaps.put("fathertablename", textField_10);
		tablePropMaps.put("mode", textField_11);
		tablePropMaps.put("authmode", textField_12);
		tablePropMaps.put("keyfieldname", textField_13);
		tablePropMaps.put("groupkeyfield", textField_14);

		fieldPropMaps.put("field_name", textField_4);
		fieldPropMaps.put("field_desc", textField_5);
		fieldPropMaps.put("field_length", textField_6);
		fieldPropMaps.put("field_dec", textField_7);
		fieldPropMaps.put("field_null", textField_8);
		fieldPropMaps.put("field_default", textField_9);
		fieldPropMaps.put("type", textField_15);
		fieldPropMaps.put("forignkey", textField_16);
	}

	private void saveTable() {
		if (index_tableLastSelect < 0 || index_tableLastSelect > model.getSize()) {
			return;
		}
		if (curField != null && index_fieldLastSelect > 0 && index_fieldLastSelect < fieldModel.getSize()) {
			FieldDto field = (FieldDto) fieldModel.get(index_fieldLastSelect);
			updateFieldProByUI(field);
		}
		ComUtils.updatePropByUItxtMap(TableDto.class, curTableDto, tablePropMaps);
		List<FieldDto> fields = ComUtils.getListObjs(FieldDto.class, fieldModel);
		curTableDto.setFields(fields);
		if (!isDataChange()) {
			return;
		}
		int flag = JOptionPane.showConfirmDialog(Global.MAIN, "数据以改变，确认修改吗?", "", JOptionPane.YES_NO_OPTION);
		if (flag != JOptionPane.OK_OPTION) {
			return;
		}
		model.setElementAt(curTableDto, index_tableLastSelect);
	}

	private void insert2fields() {
		int toIndex = (Integer) spinner.getValue();
		if (toIndex < 1 || toIndex > fieldModel.size()) {
			JOptionPane.showMessageDialog(Global.MAIN, "请输入 1 到" + (fieldModel.size()) + " 之间的数");
			return;
		}
		toIndex--;
		int index = list_1.getSelectedIndex();
		if (index == toIndex) {
			return;
		}
		if (index < 0) {
			JOptionPane.showMessageDialog(Global.MAIN, "请选择要改变的项");
			return;
		}
		Object obj = fieldModel.get(index);
		fieldModel.remove(index);
		fieldModel.insertElementAt(obj, toIndex);
		list_1.setSelectedValue(obj, true);
		ComUtils.resortNum(FieldDto.class, "sortnum", fieldModel);
	}

	private void setIndexBtn(TableDto table) {
		StringBuilder txt = new StringBuilder("索引");
		int size = 0;
		if (table != null) {
			if (table.getIndexs() != null) {
				size = table.getIndexs().size();
			}
		}
		txt.append("(").append(size).append(")");
		btnNewButton.setText(txt.toString());

	}

	private void updateUIBycurTable(TableDto table) {
		ComUtils.updateTxtUIByPropMap(TableDto.class, table, tablePropMaps);
		fieldModel.removeAllElements();
		if (table != null) {
			List<FieldDto> fileds = table.getFields();
			for (FieldDto field : fileds) {
				fieldModel.addElement(field);
			}
		}
		setIndexBtn(curTableDto);
	}

	private void updateUIBycurField(FieldDto field) {
		ComUtils.updateTxtUIByPropMap(FieldDto.class, field, fieldPropMaps);
		String fieldtype = "C";
		if (field != null) {
			fieldtype = field.getField_type();
		}
		if ("C".equalsIgnoreCase(fieldtype)) {
			radioButton_1.setSelected(true);
		} else {
			radioButton.setSelected(true);
		}
	}

	private boolean isDataChange() {
		if (curTableDto == null || index_tableLastSelect < 0) {
			return false;
		}
		TableDto orignal = (TableDto) model.getElementAt(index_tableLastSelect);
		if (!FileUtils.createInitDataTableEle(curTableDto).asXML().equals(FileUtils.createInitDataTableEle(orignal).asXML())) {
			return true;
		}
		if (!FileUtils.createStrutTableEle(curTableDto).asXML().equals(FileUtils.createStrutTableEle(orignal).asXML())) {
			return true;
		}
		return false;
	}

	private void updateTableProByUI(TableDto table) {
		ComUtils.updatePropByUItxtMap(TableDto.class, table, tablePropMaps);
		List<FieldDto> fields = ComUtils.getListObjs(FieldDto.class, fieldModel);
		table.setFields(fields);
	}

	private void fieldSelectChangeUpdate() {
		if (index_fieldLastSelect < 0 || index_fieldLastSelect > fieldModel.getSize() - 1) {
			curField = null;
			return;
		}
		FieldDto field = (FieldDto) fieldModel.get(index_fieldLastSelect);
		updateFieldProByUI(field);
		curField = null;

	}

	private void fieldSelectCahngeUIUpdate() {
		index_fieldLastSelect = list_1.getSelectedIndex();
		if (index_fieldLastSelect < 0 || index_fieldLastSelect > fieldModel.getSize()) {
			index_fieldLastSelect = -1;
		} else {
			curField = (FieldDto) fieldModel.getElementAt(index_fieldLastSelect);
		}
		updateUIBycurField(curField);
	}

	private void updateFieldProByUI(FieldDto fieldDto) {
		ComUtils.updatePropByUItxtMap(FieldDto.class, fieldDto, fieldPropMaps);
		String field_type = "C";
		if (radioButton.isSelected()) {
			field_type = "N";
		}
		fieldDto.setField_type(field_type);
	}

	private void init() {
		model.clear();
		fieldModel.clear();
		curTableDto = null;
		curField = null;
		index_tableLastSelect = -1;
	}
}
