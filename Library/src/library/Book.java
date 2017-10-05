package library;

import java.util.Random;

public class Book {

	String title;
	String author;
	String genre; // enum?
	String id;
	Patron checkedOutBy;
	
	
	public Book(String title, String author, String genre) {
		this.title = title;
		this.author = author;
		this.genre = genre;
	}
	
	public Book()
	{
		this.title = generateBookTitle();
		this.author = generateAuthor();
		this.genre = generateGenre();
		
	}
	
	public static String generateBookTitle() // TODO can add templates or length restrictions
	{
		Phrase p = new Phrase();
		p.generate();
		int wordCount = 0;
		while (wordCount++ < 4 && new Random().nextInt(3) != 2)
			p.generate();
		return p.toString();
	}	
	
	public static String generateAuthor()
	{
		String[] name = Phrase.generateName();
		return name[0] + " " + name[1]; // TODO implement
	}	
	
	public static String generateGenre() 
	{
		return Phrase.generateGenre();
	}	
	
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getGenre() {
		return genre;
	}
	public String getId() {
		return id;
	}
	public Patron getCheckedOutBy() {
		return checkedOutBy;
	}
	
	
}
