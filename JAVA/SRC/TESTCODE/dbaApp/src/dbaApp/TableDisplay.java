package dbaApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JList;
import java.awt.Dimension;

public class TableDisplay extends JFrame {

	private JPanel contentPane;
	private JTable dataTable;

	final private JScrollPane scrollPane;
	final DBConnector db = new DBConnector();
	private JTextField StudentIdField;
	private JTextField fnameField;
	private JTextField lnameField;
	
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JList<String> tableNameList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TableDisplay frame = new TableDisplay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TableDisplay() {
		try 
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel dataViewPanel = new JPanel();
		tabbedPane.addTab("Data View", null, dataViewPanel, null);
		dataViewPanel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		dataViewPanel.add(scrollPane, BorderLayout.CENTER);
		
		dataTable = new JTable();
		scrollPane.setViewportView(dataTable);
		
		JPanel panel = new JPanel();
		dataViewPanel.add(panel, BorderLayout.SOUTH);
		
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete);
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				//System.out.println(db.getColumnNames("student")[0].toString()+dataTable.getValueAt(dataTable.getSelectedRow() , 0));
				
				if(JOptionPane.showConfirmDialog(contentPane, "Are you sure you want to delete?", "Confirm", 0) == 0)
				{
					db.deleteRow("student", db.getColumnNames("student")[0].toString(), dataTable.getValueAt(dataTable.getSelectedRow() , 0).toString());
					
					refreshData();
				}
				
			}
		});
		
		JPanel dataInsertPanel = new JPanel();
		tabbedPane.addTab("Data Insert", null, dataInsertPanel, null);
		dataInsertPanel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JPanel panel_2 = new JPanel();
		dataInsertPanel.add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblStudentId = new JLabel("Student ID");
		panel_2.add(lblStudentId);
		
		StudentIdField = new JTextField();
		panel_2.add(StudentIdField);
		StudentIdField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		dataInsertPanel.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblFirstName = new JLabel("First Name");
		panel_3.add(lblFirstName);
		
		fnameField = new JTextField();
		panel_3.add(fnameField);
		fnameField.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		dataInsertPanel.add(panel_4);
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblLastName = new JLabel("Last Name");
		panel_4.add(lblLastName);
		
		lnameField = new JTextField();
		panel_4.add(lnameField);
		lnameField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		dataInsertPanel.add(lblNewLabel);
		
		JPanel panel_5 = new JPanel();
		dataInsertPanel.add(panel_5);
		
		JButton btnNewButton_1 = new JButton("Insert");
		panel_5.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [] values = {StudentIdField.getText(), fnameField.getText(), lnameField.getText()};
				db.insert("student",values);
				refreshData();
			}
		});
		
		JPanel toolBarPanel = new JPanel();
		contentPane.add(toolBarPanel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Refresh data");
		toolBarPanel.add(btnNewButton);
		
		
		
		tableNameList = new JList<String>(model);
		
		JScrollPane tableNameScrollPane = new JScrollPane();
		contentPane.add(tableNameScrollPane, BorderLayout.WEST);
		tableNameScrollPane.setPreferredSize(new Dimension(100, 20));
		tableNameScrollPane.setViewportView(tableNameList);
		//dataViewPanel.add(tableNameList, BorderLayout.WEST);
		tableNameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tableNameList.setLayoutOrientation(JList.VERTICAL);
		tableNameList.setVisibleRowCount(-1);
		
		/*db.setLoginInfo(
						"jdbc:oracle:thin:@" + 
						JOptionPane.showInputDialog("Enter host address") + ":1521:" +
						JOptionPane.showInputDialog("Enter SID"),
						JOptionPane.showInputDialog("Enter username"),
						JOptionPane.showInputDialog("Enter password"));*/
		
		db.connect();
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshData();
			}
		});
		
		//refreshData();
	}

	public void refreshData()
	{
		String[][] report = db.getQueryRows("student","studentid");

		Object[] names = db.getColumnNames("student");
		dataTable = new JTable(report, names);
		scrollPane.setViewportView(dataTable);
		//scrollPane.revalidate();
		
		
		//DefaultListModel<String> model = new DefaultListModel<String>();
		model.clear();
		tableNameList = new JList<String>(model);
		String[] tableNames = db.getDatabaseTables();
		
		for(int i = 0 ; i < tableNames.length ; i++)
		{
			model.addElement(tableNames[i]);
		}
		
		
		
		
	}
	
}
