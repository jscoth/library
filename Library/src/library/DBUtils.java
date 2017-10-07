package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * DBUtils is a class that contains all the database functions we need for 
 * our A02 library project. Including adding items, deleting items, 
 * 
 * 
 * 
 * @author Garret
 * @since 10/7/2017
 */
public class DBUtils{
	//The connection url
	private static final String connectionUrl = "jdbc:derby:DB;create=true";
	
	
	/*
	 * Author: Garret
	 * Method to add a book into the database. Could be implemented by taking in a book object instead, but 
	 * might save a step by just adding it to the database. We'll see if we need the extra book object functionality.
	 */
	public static void addBook(String title, String author, String genre) {
			
			
			// TODO add in the book id
	
			try {	
					Connection connection = DriverManager.getConnection(connectionUrl);
					Statement statement = connection.createStatement();
					
					int newID = countBooks() + 1;
					
					statement.execute("INSERT INTO Books VALUES" // TODO move this to book class
						+ "(" + newID + ",'" + title + "','" + author + "','" + genre + "', '2000-01-01', '2000-01-01')");
				} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
		}
	
	
	/*
	 * Author: Garret
	 * Method to add a patron into database.
	 */
	public static void addPatron(String firstName, String lastName) {

		try {	
				Connection connection = DriverManager.getConnection(connectionUrl);
				Statement statement = connection.createStatement();
				
				int newID = countPatrons() + 1;
				
				statement.execute("INSERT INTO Patrons VALUES" // TODO move this to book class
					+ "(" + newID + ", '" + firstName + "', '" + lastName + "')");
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Author: Garret
	 * Working method that takes in which table you want and the row(s) selected for deletion.
	 * @param table
	 * @param row
	 */
	public static void removeByID(String table, int row)  {
		
		try {	
				Connection connection = DriverManager.getConnection(connectionUrl);
				Statement statement = connection.createStatement();
				
				
				String whereCondition = (table.equalsIgnoreCase("books") ? "BookID" : "PatronID");
				
				PreparedStatement st = connection.prepareStatement("DELETE FROM " + table + 
						" WHERE " + whereCondition + " = ?");
				st.setLong(1, row);
				st.executeUpdate();
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Method queryData returns a ResultSet from a 
	 */
		public static ResultSet queryData(String table, String orderBy, String...columns) {
				
				// validate inputs
				table = table.replaceAll("[;']", "");
				orderBy = orderBy.replaceAll("[;']", "");
				
		
				try {	
						Connection connection = DriverManager.getConnection(connectionUrl);
						Statement statement = connection.createStatement();
						
						// build a properly formatted string out of the column arguments:
						StringBuilder columnsSB = new StringBuilder();
						for (String es : columns)
						{
							columnsSB.append(es + ", ");
						}
						// this will take out the last comma and space:
						columnsSB.delete(columnsSB.length()-2, columnsSB.length()); 
						
						ResultSet resultset = statement.executeQuery("SELECT " + columnsSB.toString() + " FROM " + table + " ORDER BY " + orderBy);
						return resultset;
					} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
				return null;
			}
	
	
	
	/**
	 * ResultSetToTableModel will take a result set and return a TableModel for a Jtable to 
	 * be formatted to. Very simple yet robust. Inspired from rs2xml.jar; package net.proteanit.sql;
	 * DbUtils class. Our implementation will allow the whole rs2xml to not be referenced.
	 */
	public static TableModel resultSetToTableModel(ResultSet rs) {
		try {
		    ResultSetMetaData metaData = rs.getMetaData();
		    int numberOfColumns = metaData.getColumnCount();
		    Vector<String> columnNames = new Vector<String>();
	
		    // Get the column names
		    for (int column = 0; column < numberOfColumns; column++) {
			columnNames.addElement(metaData.getColumnLabel(column + 1));
		    }
	
		    // Get all rows.
		    Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
	
		    while (rs.next()) {
			Vector<Object> newRow = new Vector<Object>();
	
			for (int i = 1; i <= numberOfColumns; i++) {
			    newRow.addElement(rs.getObject(i));
			}
	
			rows.addElement(newRow);
		    }
	
		    return new DefaultTableModel(rows, columnNames);
		} catch (Exception e) {
		    e.printStackTrace();
	
		    return null;
		}
	    }

	/*
	 * Method to check out books.
	 * 
	 * @Author: Jack 
	 */
	public static void checkOut()
	{
		try 
		{
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			statement.execute("UPDATE Books SET CheckedOut");

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}				
	}
	
	
	/*counts the number of Patrons in the library system
	 * 
	 */
	public static int countPatrons()
	{
		try 
		{
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Patrons");
			rs.next();
			return rs.getInt(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
		return -1;
	}
	
	
	/*counts the number of books in the library
	 * 
	 */
	public static int countBooks()
	{
		try 
		{
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Books");
			rs.next();
			return rs.getInt(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
		return -1;
	}
}
