package dbaApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridLayout;

public class TableDisplay extends JFrame {

	private JPanel contentPane;
	private JTable table;

	final private JScrollPane scrollPane;
	final DBConnector db = new DBConnector();
	private JTextField StudentIdField;
	private JTextField fnameField;
	private JTextField lnameField;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		tabbedPane.addTab("Data View", null, scrollPane, null);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Data Insert", null, panel_1, null);
		panel_1.setLayout(new GridLayout(0, 3, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblStudentId = new JLabel("Student ID");
		panel_2.add(lblStudentId);
		
		StudentIdField = new JTextField();
		panel_2.add(StudentIdField);
		StudentIdField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblFirstName = new JLabel("First Name");
		panel_3.add(lblFirstName);
		
		fnameField = new JTextField();
		panel_3.add(fnameField);
		fnameField.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblLastName = new JLabel("Last Name");
		panel_4.add(lblLastName);
		
		lnameField = new JTextField();
		panel_4.add(lnameField);
		lnameField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		panel_1.add(lblNewLabel);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		
		JButton btnNewButton_1 = new JButton("Insert");
		panel_5.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [] values = {StudentIdField.getText(), fnameField.getText(), lnameField.getText()};
				db.insert("student",values);
			}
		});
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Refresh data");
		panel.add(btnNewButton);
		
		db.connect();
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshData();
			}
		});
		
		refreshData();
	}

	public void refreshData()
	{
		String[][] report = db.getQueryRows();

		String[] names = {"student ID","first name","last name"};
		table = new JTable(report, names);
		scrollPane.setViewportView(table);
		scrollPane.revalidate();
	}
	
}