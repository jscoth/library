package library;
import java.util.Random;

public class Tester {

	public static void main (String[] args){
		
		/*
		for (int i = 0; i < 10 ; i++)
		{
			Phrase p = new Phrase();
			p.generate();
			int counter = 0;
			while (counter++ < 4 && new Random().nextInt(3) != 2)
				p.generate();
				
			System.out.println(p.toString());
		}
		*/
		for (int i = 0; i < 10 ; i++)
		{
		Book b = new Book();
		System.out.printf("INSERT INTO Books VALUES" // TODO move this to book class
				+ "("+ i + ",'" + b.getTitle() + "','" + b.getAuthor() + "','" + b.getGenre() + "', 0, 0)\n");
		}
		
	}
	
}
