package library;

public class Patron {
	String id;
	String firstName;
	String lastName;
	
	public Patron() {
		String[] name = Phrase.generateName();
		firstName = name[0];
		lastName = name[1]; 
	}
	
	
	public Patron(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	
	
}
