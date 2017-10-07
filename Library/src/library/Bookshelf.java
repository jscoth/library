package library;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bookshelf extends JPanel{

	private JPanel frame;
	private static String[] bookArr;
	private Random rand = new Random();
	private JPanel cover;
	private int numBooks;
	private int tail;
	private int head;
	private JTextPane titlePane;
	private JTextPane authorPane;
	private JTextPane last;
	private SpringLayout springLayout;
	
	/**
	 * Launch the application.
	 */
	public static void Launch(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Bookshelf window = new Bookshelf(bookArr);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Boolean isEmpty() {
		if (bookArr == null)
				return true;
		return false;
	}
	/**
	 * Create the application.
	 */
	public Bookshelf(String...args) {
		if (!isEmpty())
		{
			addToBookArray(args);
		}

		initialize();

	}

	private void addToBookArray(String... args) {
		System.out.println("bookarray called");
		ArrayList<String> currentList = new ArrayList<>();
		
		// add current books, if any
		if (!isEmpty())	
		{
			for (String book : bookArr)
			{
				currentList.add(book);
			}	
		}
		
		// add new books
		for (String book : args)
		{
			currentList.add(book);
		}
		
		bookArr = currentList.toArray(new String[currentList.size()]);
		System.out.println("ba: " + Arrays.toString(bookArr));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = this;
		frame.setToolTipText("\"bookshelf\"");
		frame.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, 
				Color.black,
				Color.black,
				(Color.black).brighter(),
				Color.blue
				));
		frame.setVisible(true);
		frame.setOpaque(true);
	    springLayout = new SpringLayout();
		frame.setLayout(springLayout);
		frame.setBackground(new Color(205,133,65));




		head = 0; 
		tail = 0; 

		// COVER
		// this panel represents the cover of the book the user has moused over
		cover = createCover(springLayout);
		//frame.add(cover);
		
		// title on book cover
		titlePane = new JTextPane();
		// author on book cover
		authorPane = new JTextPane();
		
		configureCoverTextPane(springLayout, cover, titlePane);
		configureCoverTextPane(springLayout, cover, authorPane);
		
		cover.add(titlePane);
		cover.add(authorPane);
		
	}
	public void add(String title) {
		addToBookArray(title); // inefficient i guess but no time
	}
	public void drawBooks() {

		
		last = null; // we use this in the loop later
		ArrayList<ArrayList<String>> Books = DBManager.queryDataConsole("Books", "Title","Title IN (" + sqlStringFormatter(bookArr) + ")", 0,"Title", "Author","Genre");
		numBooks = Books.size();
		
		System.out.println(numBooks);
		
		int maxSize = 10;
		if (numBooks > maxSize)
			tail = maxSize;
		else
			tail = numBooks;
		
		frame.removeAll();  // to fix the color/ size switching when you add new books we need to replace all the "put client property" assignments
							// with entries in a has table stored outside those objects (with the key as the index location in the "stack")
							//since now that  they are destroyed in this step we can no longer access that data. 
		for (int i = head; i < tail; i++)  
		{

			JTextPane currentBook = new JTextPane();         
			// a random descriptor that will show up on the cover
			String descriptor = Phrase.getRandomFromArray(new String[] {"Bestseller","Bocument","E-book","Exposition","Song","Novel","Tale","Story"});
			currentBook.putClientProperty( "descriptor", descriptor ); 
			// store the current i in the object so we can access it from the listener
			currentBook.putClientProperty( "bookIndex", i ); 

			// add the current title as text
			currentBook.setText(Books.get(i).get(0));

			frame.add(currentBook);		

			Color coverColor = new Color(rand.nextInt(2000 % 255),rand.nextInt(1000 % 255),rand.nextInt(1000 % 255));
			
			currentBook.putClientProperty("coverColor", coverColor); 
			makeBookBorder(currentBook, coverColor);	
			
			currentBook.putClientProperty("textColor", getComplementaryColor(coverColor));
	
		
			// the width of the book
			currentBook.putClientProperty("width", 150 + rand.nextInt(55));
			// set the background and text of the label to our new colors
			currentBook.setBackground((Color) currentBook.getClientProperty("coverColor"));
			currentBook.setForeground((Color) currentBook.getClientProperty("textColor"));
			currentBook.setOpaque(true);	
			currentBook.setVisible(true);
			System.out.println(currentBook.getPreferredSize().toString());
			setRandomFont(currentBook);
			
			//split text into two lines if needed
			String[] sp = splitAlign(currentBook.getText());
			String result ="";
			for (String s : sp)
			{

				 result +=  s.trim() + "\n";
				
			}
			result = result.substring(0, result.length()-1); // trim off last newline
			currentBook.setText(result);


	
			springLayout.putConstraint(SpringLayout.EAST, currentBook, (int) currentBook.getClientProperty("width"), SpringLayout.WEST, frame.getParent());
			springLayout.putConstraint(SpringLayout.WEST, currentBook, 12, SpringLayout.WEST, frame.getParent());
			springLayout.putConstraint(SpringLayout.SOUTH, currentBook, 55, SpringLayout.NORTH, currentBook);
			
			// last not initizlied yet so we use the top of the frames
			if (i == head)
				springLayout.putConstraint(SpringLayout.NORTH, currentBook, 8, SpringLayout.NORTH,frame.getParent());	
			else 
				springLayout.putConstraint(SpringLayout.NORTH, currentBook, 8, SpringLayout.SOUTH,last); // after the 1st loop last will be initialized
			System.out.println("last just b4 assignment : " + last);
			last = currentBook; // set this variable at the end of the loop so we can use it next time through the loop
			
			
			System.out.println(currentBook);
			// LISTENERS
			// i'm not sure where these should be in the code
			// we could do this on click instead if we want
			
			currentBook.addMouseListener(new MouseAdapter() {
				
				// when the user mouses over, we change the displayed cover to match this one:
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
					cover.setBackground((Color) currentBook.getClientProperty("coverColor"));
					String descriptor = (String) currentBook.getClientProperty("descriptor");
					String title = Books.get((int) currentBook.getClientProperty("bookIndex")).get(0);
					String genre = Books.get((int) currentBook.getClientProperty("bookIndex")).get(2);
					String article = "";
					if (Phrase.startsWithVowel(genre))
						article = "An ";
					else
						article = "A ";
					String author = article + genre + " " + descriptor + " by " +
							Books.get((int) currentBook.getClientProperty("bookIndex")).get(1);
					updateCoverElement(Books, titlePane, currentBook, title,19);
					updateCoverElement(Books, authorPane, currentBook, author,16);
					

				}

				/**
				 * @param Books
				 * @param titlePane
				 * @param currentBook
				 * @param column
				 */
				private void updateCoverElement(ArrayList<ArrayList<String>> Books, JTextPane titlePane,
						JTextPane currentBook, String text, int size) {
					titlePane.setForeground((Color) currentBook.getClientProperty("textColor"));
					titlePane.setFont((Font) currentBook.getClientProperty("font"));	
					titlePane.setFont( new Font(titlePane.getFont().getFontName(),Font.BOLD,size));

					String[] sp = splitAlign(text);
					String result ="";
					for (String s : sp)
					{
						for (String t : splitAlign(s))
						{
						 result +=  t.trim() + "\n";
						}
					}
					result = result.substring(0, result.length()-1);
					titlePane.setText(result);
				}
			});
		} // END LOOP that populates books
		//frame.revalidate();
		//frame.repaint();
	}

	private String sqlStringFormatter(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (String es : array)
		{
			sb.append("'" + es + "', ");
		}
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}



	/**
	 * this is kind of messed up but it works
	 * @param springLayout
	 * @param cover
	 * @param pane
	 * @return
	 */
	private SimpleAttributeSet configureCoverTextPane(SpringLayout springLayout, JPanel cover, JTextPane pane) {
		pane.setOpaque(false);
		StyledDocument doc = pane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		springLayout.putConstraint(SpringLayout.NORTH, pane, 20, SpringLayout.NORTH, cover);
		springLayout.putConstraint(SpringLayout.NORTH, pane, -50, SpringLayout.SOUTH, pane);
		return center;
	}

	/**
	 * @param coverColor
	 */
	private Color getComplementaryColor(Color coverColor) {
		float[] hSBVal = Color.RGBtoHSB(coverColor.getRed(),
										coverColor.getBlue(),
										coverColor.getGreen(), null);
		float[] cVals = new float[3];
		Color tColor = new Color(Color.HSBtoRGB(hSBVal[0] + (float)0.5,hSBVal[1],hSBVal[2]+(float).4)); // invert the color
		float total = 0;
		for (float e : cVals)
		{
			total += e;
		}
		total = (float) (total / 3.0);


		if (total > .5)
			return Color.WHITE; 
		else
			return Color.BLACK;//tColor.brighter();
	}

	/**
	 * Give a JTextPane a random font from the system
	 * @param currentBook
	 */
	private void setRandomFont(JTextPane currentBook) {
		// randomize fonts - TODO needs work for readability
		String[] possibleFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().
				getAvailableFontFamilyNames() ;

		currentBook.putClientProperty("font", new Font(possibleFonts[rand.nextInt(possibleFonts.length)], Font.BOLD, 15)); //(rand.nextInt(3)+12)));
		currentBook.setFont((Font) currentBook.getClientProperty("font"));
	}

	/*
	 * add a book border to a jTextPane
	 */
	public static void makeBookBorder(JTextPane currentBook, Color color) {
		currentBook.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createBevelBorder(BevelBorder.RAISED, 
										(color).darker(),
										color,
										(color).brighter(),
										Color.orange
										), 
								BorderFactory.createBevelBorder(BevelBorder.RAISED, 
										color,
										color,
										(color).brighter(),
										Color.orange
										)
								)
					 );
	}

	private JPanel createCover(SpringLayout springLayout) {
		JPanel cover = new JPanel();
		cover.setBorder(BorderFactory.createLineBorder(Color.black));
		cover.setOpaque(true); 
		cover.setBackground(Color.pink);	
		springLayout.putConstraint(SpringLayout.EAST, cover, -40, SpringLayout.EAST, frame); // the EAST edge of cover is -20 pixels from the EAST edge of the frame
		springLayout.putConstraint(SpringLayout.NORTH,cover, 20, SpringLayout.NORTH, frame); // likewise with north
		springLayout.putConstraint(SpringLayout.SOUTH, cover, 325, SpringLayout.NORTH, cover); // the SOUTH edge of the cover is 300 pixels away from the NORTH edge of cover
		springLayout.putConstraint(SpringLayout.WEST, cover, -225, SpringLayout.EAST, cover);  // etc
		return cover;
	}

	/*
	 * splits text into two lines if it is too long
	 */
	private String[] splitAlign(String text) {
		if (text.length() > 15)
		{
			int splitPoint = text.indexOf(" ", text.length( ) / 2) + 1; //find the index of the first space after the halfway point.
			// return -1 if no match but we added 1
			if (splitPoint == 0) // if there wasn't another word split at the first space
				splitPoint = text.indexOf(" "); 
			if (splitPoint == 0) // if there wasn't a space give up
			{
				String[] rVal = {text};
				return rVal;
			}
			 String[] rVal = {(text.substring(0, splitPoint)),(text.substring(splitPoint))};
			return rVal; 
		} 
		String[] rVal = {text};
		return rVal;
		
	}
}
