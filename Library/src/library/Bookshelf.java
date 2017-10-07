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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bookshelf extends JPanel{

	private JPanel frame;
	private static String[] bookArr;
	private JPanel cover;
	private int numBooks;
	private int tail;
	private int head;
	private JTextPane titlePane;
	private JTextPane authorPane;
	private JTextPane last;
	private SpringLayout springLayout;
	private BookStack bStack = new BookStack();
	
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
	 * @throws Exception 
	 */
	public Bookshelf(String...args) throws Exception {
		if (!isEmpty())
		{
			addToBookArray(args);
		}

		initialize();

	}

	private void addToBookArray(String... args) throws Exception {
		for (String item : args)
		{
			System.out.println("item: " + item);
			bStack.push(item);
		}
		bookArr = bStack.toArray();
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
	public void add(String title) throws Exception {
		addToBookArray(title); // inefficient i guess but no time
	}
	public void drawBooks() {

		
		last = null; // we use this in the loop later
		ArrayList<ArrayList<String>> Books = DBManager.queryDataConsole("Books", "Title","Title IN (" + sqlStringFormatter(bookArr) + ")", 0,"Title", "Author","Genre","Color","Font","Width","descriptor");
		
		numBooks = Books.size();
		
		System.out.println(numBooks);
		
		int maxSize = 15;
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
		
			// store the current i (location in the list) in the object so we can access it from the listener
			currentBook.putClientProperty( "bookIndex", i ); 

			// add the current title as text
			currentBook.setText(Books.get(i).get(0));

			frame.add(currentBook);		

			// set the background and text of the label to our new colors
			String[] rawColor = Books.get(i).get(3).split(",");
			currentBook.setBackground(new Color(
											Integer.valueOf(rawColor[0]),  //R
											Integer.valueOf(rawColor[1]),  //G
											Integer.valueOf(rawColor[2]))  //B
										 );
			currentBook.setForeground(getComplementaryColor(currentBook.getBackground()));
			
			// add booky border
			makeBookBorder(currentBook, currentBook.getBackground());
			
			currentBook.setOpaque(true);	
			currentBook.setVisible(true);
			currentBook.putClientProperty("font", new Font(Books.get(i).get(4),Font.BOLD,14));
			//setRandomFont(currentBook);
			
			springLayout.putConstraint(SpringLayout.EAST, currentBook, Integer.valueOf(Books.get(i).get(5)), SpringLayout.WEST, frame.getParent());
			springLayout.putConstraint(SpringLayout.WEST, currentBook, 12, SpringLayout.WEST, frame.getParent());
			
			// last not initizlied yet so we use the top of the frames
			if (i == head)
				springLayout.putConstraint(SpringLayout.SOUTH, currentBook, 0, SpringLayout.SOUTH,frame);	
			else 
				springLayout.putConstraint(SpringLayout.SOUTH, currentBook, -2, SpringLayout.NORTH,last); // after the 1st loop last will be initialized

			last = currentBook; // set this variable at the end of the loop so we can use it next time through the loop
			
		
			// LISTENERS
			
			currentBook.addMouseListener(new MouseAdapter() {
				
				// when the user mouses over, we change the displayed cover to match this one:
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
					int index = (int) currentBook.getClientProperty("bookIndex");
					//parse color info
					String[] rawColor = Books.get((int) currentBook.getClientProperty("bookIndex")).get(3).split(",");
					cover.setBackground(new Color(
													Integer.valueOf(rawColor[0]),  //R
													Integer.valueOf(rawColor[1]),  //G
													Integer.valueOf(rawColor[2]))  //B
												 );
					
					String descriptor = Books.get(index).get(6);
					String title = Books.get(index).get(0);
					String genre = Books.get(index).get(2);
					
					// construct author / genre / descriptor string
					// i.e. "a sci fi novel by Joe Schmoe"
					String article = "";
					if (Phrase.startsWithVowel(genre))
						article = "An ";
					else
						article = "A ";
					String author = article + genre + " " + descriptor + " by " +
							Books.get(index).get(1);
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
					titlePane.setText(text); //setText(result);   // bypassed for now
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

	/*
	 * add a book border to a jTextPane
	 */
	public static void makeBookBorder(JTextPane currentBook, Color color) {
		currentBook.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createBevelBorder(BevelBorder.RAISED, 
										(color).brighter(),
										Color.orange,
										(color).darker(),
										color
										), 
								BorderFactory.createBevelBorder(BevelBorder.RAISED, 
										(color).brighter(),
										Color.orange,
										(color).darker(),
										color
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
		if (text.length() > 30)
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
