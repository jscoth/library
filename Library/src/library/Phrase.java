package library;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Phrase {

	private Word top;
	private Random rand = new Random();
	
	/*
	 * push a new word onto the stack
	 */
	public Boolean isEmpty()
	{
		return (top == null);
	}
	
	/* generate a random word and add it to the stack.
	 * the word will be picked based on the type of word that is currently on top of the stack
	 */
	public void generate()
	{
		
		//TODO logic for how much words to add
		
		// TODO - templates?
		
		// when called on an empty stack, pick a noun or verb to start with
		if (isEmpty())
		{
			if (rand.nextBoolean())
				top = createRandomWord("noun"); 
			else
				top = createRandomWord("verb");
			return;
		}
		
		String[] options = top.getNextOption();
		
		// select a random part of speech from the options array
		String partOfSpeech = options[rand.nextInt(options.length)]; 
		
		// and add it to the stack
		Word tempWord = top;
		top = createRandomWord(partOfSpeech);
		top.setNext(tempWord);	
		
		// don't begin a phrase with a conjunction - if we pull one, then run generate again to put something else in front
		if (partOfSpeech.contentEquals("conjunction"))
			this.generate();
	}
	
	/**
	 * Author: Garret 
	 * This is the simple way to get a first and last name and return it as a string.
	 * @return
	 */
	public static String[] generateName() // new method
	{
		return new String[] {getRandomLineFromFile(".\\words\\names\\first.txt") , getRandomLineFromFile(".\\words\\names\\last.txt")};
	}
	/**
	 * Author: Garret 
	 * This is the simple way to get a genre from a file and return it as a string.
	 * @return
	 */
	public static String generateGenre() {
		
		return getRandomLineFromFile(".\\words\\genres\\genres.txt");
	}

	/**Author: Garret
	 * Returns a random line from a file
	 */
	private static String getRandomLineFromFile(String filePath) {
		String output = "";
		try {
				Random rand = new Random();
				// open the file
				File file = new File(filePath);
				// go to a random spot in it
				RandomAccessFile fileIn = new RandomAccessFile(file,"r");
				
				fileIn.seek(rand.nextInt((int)fileIn.length()));
				fileIn.readLine();
				output = fileIn.readLine();
				//output = output.substring(0, 1).toUpperCase() + output.substring(1);
				// close input
				fileIn.close();
				return output;
			} 
		catch (FileNotFoundException a)  {a.printStackTrace();}
		catch (IOException a) 		     {a.printStackTrace();}
		return output;
	}
	/*
	 * Creates a new Word object of the approriate part of speech
	 */
	
	public Word createRandomWord(String partOfSpeech) 
	{
		// TODO handle bad arguments
		String filepath;  // where the list of words is located for this type of word
		String[] options = {""}; // this will hold an array of the parts of speech that can precede this type
		Random rand = new Random();
		Word returnWord; // some of the parts of speech use this temporary, poorly named variable
		String wd = "";
		System.out.println("started new random word");  //DEBUG
		switch (partOfSpeech.toLowerCase())
		{
		
			case "noun" : options =  new String[] {"adjective", "article", "conjunction"};
			
				wd = getRandomLineFromFile(".\\words\\nouns\\91K nouns.txt");
				break;
				
			case "conjunction" : // HOOKIN UP NOUNS AND PHRASES AND CLAUSES
				return generateConjunction(); 
				
			case "adjective" :  options =  new String[] {"article", "conjunction"};
			
				// nouns can be used as adjectives so pick one of the two
				if (new Random().nextBoolean()) 
					wd = getRandomLineFromFile(".\\words\\nouns\\91K nouns.txt");
				else
					wd = getRandomLineFromFile(".\\words\\adjectives\\28K adjectives.txt");
				break;
				
			case "article" :  
				return generateArticle();
				
			// TODO split to -ing and present tense
			case "verb" : options =  new String[] {"adjective", "noun", "conjunction", "adverb"};
			
				wd = getRandomLineFromFile(".\\words\\verbs\\31K verbs.txt");
				break;
				
			case "adverb" : options =  new String[] {"adjective", "noun", "conjunction", "verb"};
			
				wd = getRandomLineFromFile(".\\words\\adverbs\\6K adverbs.txt");	
				break;
		}

		//detect and truncate verbs ending in "s" 
		if (partOfSpeech.equals("verb") && wd.endsWith("s"))
		{
			//System.out.println("DEBUG: truncated: " + wd);
			wd = wd.substring(0, (wd.length()-1));
		}
		
		// add some pizzaz to verbs ending in 'ing' or 'ed'
		String puncuation = ":::::;??!";    // weighted towards colons since they are the most 'booky' for titles
											// best book discovered during testing: " Jitterbugging: the Aquarius Cyclamen " 
		
		if (wd.endsWith("ed")) 
			wd += puncuation.charAt(rand.nextInt(puncuation.length()));
		
		if (wd.endsWith("ing") && (rand.nextBoolean())) // 50% chance
			wd += puncuation.charAt(rand.nextInt(puncuation.length()));
		
		//add some imagination
		if (!isEmpty() && partOfSpeech.equals("noun") && (rand.nextInt(10) == 3) && !top.getPartOfSpeech().equals("conjunction")) // Not empty, Noun, 10% chance, must not be preceding a conjunction
			{
				if (rand.nextBoolean())
					wd += "?";
				else
					wd += ",";
			}
		System.out.println("inside CRW: wd is: "+ wd);
		System.out.println("inside CRW: partofspeech is: "+ partOfSpeech);
		returnWord = new Word(wd);
		returnWord.setNextOption(options);
		returnWord.setPartOfSpeech(partOfSpeech);
		return returnWord;
	}

	private Word generateArticle() {
		
		String article = "";
		
		// 50% chance of definite/indefinite article
		if (new Random().nextBoolean())
			{
					article = "the"; 
			}
		else
			{
				if (top.getText().length() > 0 && startsWithVowel(top.getText()))
					article = "an";
				else
					article = "a";
			}

		Word returnWord = new Word(article); 
		returnWord.setNextOption(new String[] {"verb"});
		returnWord.setPartOfSpeech("article");
		return returnWord;
	}
	
	public static  String getRandomFromArray(String[] array) {
		Random rand = new Random();
		return array[rand.nextInt(array.length)];
	}
	
	public static boolean arrayContains(int[] array,int chr) {
		for (int i : array)
		{
			System.out.println(new Character((char) i));
			if (i == chr)
				return true;
		}
		return false;
	}
	
	public static boolean startsWithVowel(String str) {
		int[] vowels = {97,101,105,111,117}; // ascii codes for vowels
		char firstChar = str.toLowerCase().charAt(0);
		for (int i : vowels)
		{
			if (i == firstChar)
				return true;
		}
		return false;
	}
	
	private Word generateConjunction() {
		String[] options = {"noun", "verb"};
		
		
		Word returnWord;
		
		// pick a random coordinating conjunction // TODO - change nested ifs
		if (rand.nextBoolean())
			if (rand.nextBoolean())
				returnWord = new Word("and");
			else
				returnWord = new Word("for");
		else
			{
			if (rand.nextInt(5) == 3) // 20% chance of comma 
				returnWord = new Word("or,");
			else
				returnWord = new Word("or");
			}
			
		returnWord.setNextOption(options);
		returnWord.setPartOfSpeech("conjunction");
		return returnWord;
	}
	/*
	 * Recursively travel the stack and return the resulting phrase as a string
	 */
	public String toString()
	{
		return top.toString(); 
	}

}
