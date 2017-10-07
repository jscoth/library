package library;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DBManager { // TODO change to DBmanager
	
	private static final String connectionUrl = "jdbc:derby:DB;create=true";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		clearBooks();
		Initialize();
		addRandomBooks(12);
		addRandomBooks(15);
		addRandomPatrons(35);

		
	}
	
	private static void Initialize() {
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);

			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Books("
					+ "BookID int,"  // primary key
					+ "Title varchar(256)" + ","
					+ "Author varchar(124)"  + ","
					+ "Genre varchar(64)"  + ","
					+ "PatronID int"    // foreign key - Patrons table ID
										// technically this should have the same
										// column name as it does in that table
					+ ")");
			// pretty sure we can actually chain these but the syntax gets annoying
			// TODO - look into it
			// for now we'll just reuse this variable
			statement = connection.createStatement();
			statement.execute("CREATE TABLE Patrons("
					+ "PatronID int,"
					+ "FirstName varchar(64),"
					+ "LastName varchar(64)"
					+ ")");
			System.out.println("Database created.");
			}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void addRandomPatrons(int num) {
		
		
		int counter = 0;
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Patrons");
			rs.next();
			int total = rs.getInt(1);

			while (counter <= num) // TODO - add helper function for this. USE SELECT to only update relevant rows. 
				{
					Patron p = new Patron();
					int newID = counter + total;
					statement.execute("INSERT INTO Patrons VALUES" 
							+ "("+ newID + ",'" + p.getFirstName() + "','" + p.getLastName() + "')");
					counter++;
				}
			
			} 
		catch (SQLException e) 
			{
				e.printStackTrace();
			}
		
		System.out.println("Added " + counter + " patrons.");
	}
	
	
	public static void addRandomBooks(int num) {
		
		
		int counter = 0;
		try {
			// connect and add to database. TODO - optimize, get this out of the loop
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Books");
			rs.next();
			int total = rs.getInt(1);

			while (counter <= num) // TODO - add helper function for this. USE SELECT to only update relevant rows. 
				{
					Book b = new Book();
					int newID = counter + total;
					statement.execute("INSERT INTO Books VALUES" // TODO move this to book class
							+ "("+ newID + ",'" + b.getTitle() + "','" + b.getAuthor() + "','" + b.getGenre() + "', 0)");
					counter++;
				}
			
			} 
		catch (SQLException e) 
			{
				e.printStackTrace();
			}
		
		System.out.println("Added " + counter + " books.");
	}
	

	
	@SuppressWarnings("unused")
	private static void queryData() { // don't use anymore
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery("SELECT * FROM Books");
			
			while(resultset.next()) {
				String id = resultset.getString("BookID");
				String title = resultset.getString("Title");
				String author = resultset.getString("Author");
				System.out.printf("%s: %s, by %s \n", id, title, author);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
	
	public static void add()
	{
		
	}

	/*
	 * returns an arraylist as follows:
	 * the first element is a list of the columns queried
	 * each subsequent element is a list representing 1 database row:
	 * each element in the sub-list is the corresponding data from that column
	 */
	
	// used to return ArrayList<ArrayList<String>>
	
	public static ResultSet queryData(String table, String orderBy, String...columns) {
		
		// validate inputs
		table = table.replaceAll("[;']", "");
		orderBy = orderBy.replaceAll("[;']", "");
		
		// TODO check if table exists

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

	/*
	 * updates 1 column and value in a table
	 */
	public static void updateData(String table,  String whereCondition,String columnValues)  {

		try {	
				Connection connection = DriverManager.getConnection(connectionUrl);
				Statement statement = connection.createStatement();
				System.out.println(("UPDATE " + table + " SET " + columnValues +  
						" WHERE " + whereCondition));
				statement.execute("UPDATE " + table + " SET " + columnValues +  
						" WHERE " + whereCondition);
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public static ResultSet queryData(String table, String orderBy, String whereCondition, int not, String...columns)  {
		// so the int can be any number but right now prevents ambiguity in overloaded method
		// TODO - change varargs to string array
		// validate inputs
		table = table.replaceAll("[;']", "");
		orderBy = orderBy.replaceAll("[;']", "");
		
		// TODO check if table exists

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
				System.out.println(("SELECT " + columnsSB.toString() + " FROM " + table + 
						" WHERE " + whereCondition +
						" ORDER BY " + orderBy));
				ResultSet resultset = statement.executeQuery("SELECT " + columnsSB.toString() + " FROM " + table + 
						" WHERE " + whereCondition +
						" ORDER BY " + orderBy);
				return resultset;
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	// used to return ArrayList<ArrayList<String>>
	
	public static ArrayList<ArrayList<String>> queryDataConsole(String table, String orderBy, String whereCondition ,int nothing, String...columns) {
		
		// validate inputs
		table = table.replaceAll("[;']", "");
		orderBy = orderBy.replaceAll("[;']", "");
		
		// TODO check if table exists

		
		try {
				ArrayList<ArrayList<String>> results = new ArrayList<>(); // each element in the list is itself a list of results (1 element per column)
				
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
				String q = ("SELECT " + columnsSB.toString() + " FROM " + table + 
						" WHERE " + whereCondition +
						" ORDER BY " + orderBy);
				System.out.println(q);
				ResultSet resultset = statement.executeQuery(q);
				
				ArrayList<String> bookInfo = new ArrayList<>();
				/*/ add first element with column headings:
				for (String column : columns)
				{
				bookInfo.add(column);
				}
				results.add(bookInfo);
				//*/
				
				
				
				// add in results
				while(resultset.next())
				{
					bookInfo = new ArrayList<>(); // list of results
					for (String column : columns)
					{
					bookInfo.add(resultset.getString(column));
					}
				results.add(bookInfo);		
				}
				return results;	
				
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private static void clearBooks() {
		try {
			Connection connection = DriverManager.getConnection(connectionUrl);
			Statement statement = connection.createStatement();
			statement.execute("DROP TABLE Books");
			statement.execute("DROP TABLE Patrons");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("DB cleared");
	}
	
	/*
	 * Author: Garret
	 * Method to add a book or user into the database. Could be implemented by taking in a book object instead, but 
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
	

	public static void removeByID(String table, int...rows)  {
		
		try {	
				Connection connection = DriverManager.getConnection(connectionUrl);
				Statement statement = connection.createStatement();
				
				// build a properly formatted string out of the column arguments:
				StringBuilder rowSB = new StringBuilder();
				for (int es : rows)
				{
					rowSB.append(es + ", ");
				}
				// this will take out the last comma and space:
				rowSB.delete(rowSB.length()-2, rowSB.length());
				
				String whereCondition = (table.equalsIgnoreCase("books") ? "BookID" : "PatronID");
				
				ResultSet resultset = statement.executeQuery("DELETE FROM " + table + 
						" WHERE " + whereCondition);
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ResultSetToTableModel will take a result set and return a TableModel for a Jtable to 
	 * be formatted to. Very simple yet robust. Lifted from rs2xml.jar; package net.proteanit.sql;
	 * DbUtils class. Our implementation will allow the whole rs2xml to not be referenced. 
	 * @param rs
	 * @return
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
}
