package library;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleInterface {
	
	
	public static void main(String[] args) {

		int numBooks = DBManager.countBooks();
		System.out.println("WELCOME TO THE LIBRARY: home of " + numBooks + " books. Enter command (query, add, checkout, return):");
		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();
		switch (userInput.toLowerCase())
		{
		case "query" : query(); break;
		case "add" :
			System.out.println("Enter number of books to add:");
			DBManager.addRandomBooks(input.nextInt());
			break;
			
		default : System.out.println("invalid input.");
		}
		
		
		input.close();
	}

	public static void query()
	{
		// query:
		System.out.println("Enter table, sorting column and which columns to display, delimited by commas (no spaces):");
		
		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();
		
		// first argument is the table 
		int firstComma = userInput.indexOf(","); 
		String table = userInput.substring(0,firstComma);
		userInput = userInput.substring(firstComma+1);
		
		// second argument is the order by 
		firstComma = userInput.indexOf(","); 
		String order = userInput.substring(0,firstComma);
		
		// remaining args are columns
		String[] columns = userInput.substring(firstComma+1).split(",");
		
		//ArrayList<ArrayList<String>> answer = DBManager.queryDataConsole(table,order,columns);
		//for (ArrayList<String> line : answer)
		//{
		//System.out.println(line.toString());
		//}
		
		input.close();
	}
}
