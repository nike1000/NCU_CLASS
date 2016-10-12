import java.sql.*;
import java.util.*;
import javax.swing.table.*;

public class PersonQueries extends AbstractTableModel
{
	private static final String URL="jdbc:mysql://localhost/member";
	private static final String USERNAME="root";
	private static final String PASSWORD="javaMySQL";
	
	private Connection connection;
	private String Field[]={"MemberID","name","phone","e_mail","sex"};
	private Statement statement;
	public ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	
	
	public PersonQueries()
	{
		try
		{
			connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);//建立連線
			statement=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);//取得Statement用以提交查詢
			
			BrowseAll();//顯示全部資料

		}
		catch(SQLException sqlException)
		{
			System.exit(1);
		}
	}
	
	public void BrowseAll()
	{
		try
		{
			resultSet=statement.executeQuery("SELECT*FROM people");//提交查詢
			metaData=resultSet.getMetaData();//取得資料
			resultSet.last();//將result定位到最後一列
			numberOfRows=resultSet.getRow();//取得列數
			fireTableStructureChanged();//通知JTable可更新
		}
		catch(SQLException sqlException)
		{
			
		}
	}
	
	public void Find(String Findkey,int by)
	{
		try
		{
			resultSet=statement.executeQuery("SELECT*FROM people WHERE "+Field[by]+"='"+Findkey+"'");//提交查詢
			
			metaData=resultSet.getMetaData();
			resultSet.last();
			numberOfRows=resultSet.getRow();
			fireTableStructureChanged();
		}
		catch(SQLException sqlException)
		{
			
		}
	}
	
	public void Insert(String Name,String Phone,String E_mail,String Sex)
	{
		try
		{
			statement.executeUpdate("INSERT INTO people (name,phone,e_mail,sex) VALUES"+"('"+Name+"','"+Phone+"','"+E_mail+"','"+Sex+"')");
			BrowseAll();
		}
		catch(SQLException sqlException)
		{
			
		}
	}
	
	public void Delete()
	{
		try
		{
			statement.executeUpdate("DELETE FROM people WHERE MemberID='"+DisplayTest.IDField.getText()+"'");
			BrowseAll();
		}
		catch(SQLException sqlException)
		{
			
		}
	}
	
	public void Update()
	{
		try
		{
			statement.executeUpdate("UPDATE people SET name='"+DisplayTest.NameField.getText()+"',phone='"+DisplayTest.PhoneField.getText()+"',e_mail='"+DisplayTest.EmailField.getText()+"',sex='"+DisplayTest.SexField.getText()+"' WHERE MemberID='"+DisplayTest.IDField.getText()+"'");
			BrowseAll();
		}
		catch(SQLException sqlException)
		{
			
		}
		
	}
	
	public Class getColumnClass(int column)
	{
		try
		{
			String className=metaData.getColumnClassName(column+1);
			System.out.printf("getColumnClass %d\n",column);
			return Class.forName(className);
		}
		catch(Exception exception)
		{
			
		}
		return Object.class;
	}
	
	public int getColumnCount()//JTable以此接收要顯示的行數
	{
		try
		{
			System.out.printf("getColumnCount%d\n",metaData.getColumnCount());
			return metaData.getColumnCount();
		}
		catch(SQLException sqlException)
		{
			
		}
		return 0;
	}
	
	public String getColumnName(int column)//JTable以此接收行名
	{
		try
		{
			System.out.printf("getColumnName %d\n",column);
			return metaData.getColumnName(column+1);
		}
		catch(SQLException aqlException)
		{
			
		}
		return "";
	}
	
	public int getRowCount()//JTable以此接收要顯示的列數
	{
		System.out.printf("getRowCount %d\n",numberOfRows);
		return numberOfRows;
	}
	public Object getValueAt(int row,int column)//JTable以此接收每個欄位的值
	{
		try
		{
			System.out.printf("getValueAt %d,%d\n",row,column);
			resultSet.absolute(row+1);
			
			return resultSet.getObject(column+1);
		}
		catch(SQLException sqlException)
		{
			
		}
		return "";
	}
}
