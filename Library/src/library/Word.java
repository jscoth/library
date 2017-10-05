package library;

public class Word {
	private String text;
	private String partOfSpeech; 


	private Word next;
	private String[] nextOption; // this is a list of parts of speech that are allowed to come before this word

	public Word(String text)
	{
		this.setText(text); 
		// set the list of available options based on what part of speech
		
	}
	
	/** return the child object of this word
	 *  will actually be the word that precedes it in the phrase
	 * @return
	 */
	public Word getNext() {
		return next;
	}	
	
	/* return the actual english word (text) contained in this object
	 * 
	 */
	public String getText() {
		return text;
	}
	public void setNext(Word next) {
		this.next = next;
	}
	public void setText(String txt) {
		this.text = txt;
	}	
	
	public String toString()
	{
		if (this.getNext() != null)
		{
			return text + " " + this.getNext().toString();
		}
		return text;
	}
	
	/*
	 * return what parts of speech are available to precede this word
	 */
	public String[] getNextOption()
	{
		return nextOption;
	}
	
	/* 
	 * set what parts of speech are allowed to preced this word
		 */
	public void setNextOption(String[] options)
	{
		nextOption =  options;
	}
	
	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
}
