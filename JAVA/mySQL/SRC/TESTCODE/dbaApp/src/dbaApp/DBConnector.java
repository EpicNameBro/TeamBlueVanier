package dbaApp;
import java.sql.Connection;  // for standard JDBC programs
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
// for BigDecimal and BigInteger support
public class DBConnector {

	private String URL, USER, PASS;
	private Connection connection;
	
	public DBConnector()
	{
		try {
			   //Class.forName("oracle.jdbc.driver.OracleDriver");
				Class.forName("com.mysql.jdbc.Driver");
			   setLoginInfo("jdbc:mysql://localhost:3306/testdb", "root" , "");
			   //setLoginInfo("jdbc:mysql://localhost/BlueTeam","btv","btv");
			}
			catch(ClassNotFoundException ex) {
			   JOptionPane.showMessageDialog(null,"Error: unable to load driver class!");
			   System.exit(1);
			} 
	}
	
	public DBConnector(String URL, String USER, String PASS)
	{
		setLoginInfo(URL,USER,PASS);
	}
	
	public void setLoginInfo(String URL, String USER, String PASS)
	{
		this.URL = URL;
		this.USER = USER;
		this.PASS = PASS;
	}
	
	public boolean connect()
	{
		try 
		{
			connection = DriverManager.getConnection(URL, USER, PASS);
			return true;
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
			return false;
		}
	}
	
	public void closeConnection()
	{
		try 
		{
			connection.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String[] getDatabaseTables()
	{
		try {
			Statement stmt = null;
			stmt = connection.createStatement( );
			ResultSet rs;
			rs = stmt.executeQuery("show tables");
			ArrayList<String> text = new ArrayList<String>();
			while(rs.next())
			{
		         text.add(rs.getString(1));//1st column
		    }
			
			return text.toArray(new String[text.size()]);
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
		}

		return null;
	}
	
	public String[] getColumnNames(String tableName)
	{
		try {
			Statement stmt = null;
			stmt = connection.createStatement( );
			ResultSet rs;
						
			rs = stmt.executeQuery("select column_name from information_schema.columns where table_name = '" + tableName + "'");
	
			ArrayList<String> text = new ArrayList<String>();
			
			while(rs.next())
		         text.add(rs.getString(1));//1st column
		    
			
			return text.toArray(new String[text.size()]);
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
		}

		return null;
	}
	
	public String[][] getAllRows(String tableName, String orderBy)
	{
		try 
		{
			//PreparedStatement pstmt = null;

			Statement stmt = null;
			
			stmt = connection.createStatement( );
			ResultSet rs;
			
			
			System.out.println(orderBy);
			rs = stmt.executeQuery("select * from " + tableName + " order by " + orderBy);
			
			
			
			ArrayList<ArrayList<String>> text = new ArrayList<ArrayList<String>>();
			
			String[] columnNames = getColumnNames(tableName);
			
			int rows = 0;
			while(rs.next())
			{
				ArrayList<String> t = new ArrayList<String>();
				for(int i = 0 ; i < columnNames.length ; i++)
				{
					t.add(rs.getString(columnNames[i]));
				}
				
				text.add(t);
				rows++;
		    }
			
			String[][] fText = new String[0][0];
			
			if (!text.isEmpty()) 
			{
				fText = new String[rows][text.get(0).size()];
				for (int i = 0; i < text.size(); i++) 
				{
					for (int j = 0; j < text.get(0).size(); j++) 
					{
						fText[i][j] = text.get(i).get(j);
					}
				}
			}
			
			
			//conn.close();
			
			return fText;
			
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void insert(String tableName, String[] values)
	{
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String sql = "insert into " + tableName +" values(";
			for(int i = 0 ; i < values.length ; i++)
			{
				if(i == values.length-1)
				{
					sql += "'" + values[i] + "'";
				}
				else
				{
					sql += "'" + values[i] + "',";
				}
			}
			sql += ")";
			System.out.println(sql);
			
			stmt.executeUpdate(sql);
			
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}
	
	public void update(String tableName, String primaryKeyName, String primaryKeyValue, String updateColumnName, String updateColumnValue)
	{
		
		Statement stmt = null;
		
		try 
		{
			stmt = connection.createStatement();
			stmt.executeUpdate("update " + tableName + " set " + updateColumnName + "='" + updateColumnValue + "' where " + primaryKeyName + "='" + primaryKeyValue +"'");
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}
	
	public void deleteRow(String tableName, String primaryKeyName, String primaryKeyValue)
	{
		Statement stmt = null;
		
		try 
		{
			stmt = connection.createStatement();
			stmt.executeUpdate("delete from " + tableName + " where " + primaryKeyName + "='" + primaryKeyValue + "'" );
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}

}
