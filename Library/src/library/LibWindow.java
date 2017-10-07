package library;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;

import net.proteanit.sql.DbUtils;

import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class LibWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField idField;
	private JButton submitButton;
	private JLabel lblEnterUserId;
	private final ButtonGroup bookDisplaybuttonGroup = new ButtonGroup();
	private final ButtonGroup sortButtonGroup = new ButtonGroup();
	private boolean librarian = false;
	private final String header = "Library Database Application";
	private ArrayList<String> cart = new ArrayList<>();
	private int loggedInAs;
	private Bookshelf bookshelfPanel;
	private JRadioButton radioTitle;
	private JRadioButton radioGenre;
	private JRadioButton radioAuthor;
	private JRadioButton radioShowAll;
	private JRadioButton radioShowAvail;
	private JPanel panel_1;
	private SpringLayout springLayout;
	private JPanel panel_2;
	private JTextField txtYourBooks;
	private SpringLayout springLayout_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibWindow frame = new LibWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LibWindow() {
		setTitle(header);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 565);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		springLayout = new SpringLayout();
		contentPane.setLayout(springLayout);
		
		// panel declaration
		JPanel sortPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, sortPanel, 5, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, sortPanel, 5, SpringLayout.WEST, contentPane);
		sortPanel.setPreferredSize(new Dimension(150, 100));
		contentPane.add(sortPanel, BorderLayout.WEST);
		
		// Show Books label
		JLabel btnShowBooks = new JLabel("Show Books");
		btnShowBooks.setBounds(12, 5, 150, 30);
		btnShowBooks.setPreferredSize(new Dimension(150, 30));
		sortPanel.setLayout(null);
		sortPanel.add(btnShowBooks);
		
		radioShowAll = new JRadioButton("Show All Books");
		radioShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshDBTable();
			}
		});
		bookDisplaybuttonGroup.add(radioShowAll);
		radioShowAll.setBounds(12, 46, 115, 25);
		radioShowAll.setHorizontalAlignment(SwingConstants.LEFT);
		sortPanel.add(radioShowAll);
		
		radioShowAvail = new JRadioButton("Show Available Books");
		radioShowAvail.setSelected(true);
		radioShowAvail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshDBTable();
			}
		});
		bookDisplaybuttonGroup.add(radioShowAvail);
		radioShowAvail.setBounds(12, 76, 153, 25);
		sortPanel.add(radioShowAvail);
		
		JLabel btnNewButton_1 = new JLabel("Sort Books");
		btnNewButton_1.setBounds(12, 146, 150, 30);
		btnNewButton_1.setPreferredSize(new Dimension(150, 30));
		sortPanel.add(btnNewButton_1);
		
		// ********** SORT BY AUTHOR ********** // ********** SORT BY AUTHOR **********
		radioAuthor = new JRadioButton("Author");
		radioAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshDBTable();
			}
		});
		sortButtonGroup.add(radioAuthor);
		radioAuthor.setBounds(12, 204, 91, 25);
		sortPanel.add(radioAuthor);
		
		// ********** SORT BY TITLE ********** // ********** SORT BY TITLE **********
		radioTitle = new JRadioButton("Title");
		radioTitle.setSelected(true);
		radioTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshDBTable();
			}
		});
		sortButtonGroup.add(radioTitle);
		radioTitle.setBounds(12, 233, 113, 25);
		sortPanel.add(radioTitle);
		
		// ********** SORT BY GENRE ********** // ********** SORT BY GENRE **********
		radioGenre = new JRadioButton("Genre");
		radioGenre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshDBTable();
			}
		});
		sortButtonGroup.add(radioGenre);
		radioGenre.setBounds(12, 261, 113, 25);
		sortPanel.add(radioGenre);
		
		// extracted the librarian access button to a method -j
		
		JPanel userControls = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, sortPanel, -5, SpringLayout.NORTH, userControls);
		springLayout.putConstraint(SpringLayout.SOUTH, userControls, -5, SpringLayout.SOUTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, userControls, 5, SpringLayout.WEST, contentPane);
		userControls.setPreferredSize(new Dimension(100, 100));
		contentPane.add(userControls, BorderLayout.SOUTH);
		userControls.setLayout(null);
		
		lblEnterUserId = new JLabel("Enter Card Number:");
		lblEnterUserId.setBounds(12, 29, 110, 16);
		userControls.add(lblEnterUserId);
		
		// -j
		idField = new JTextField();
		idField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				if (!idField.getText().equals("") && k.getKeyCode() == 10) // enter
				{
					int userID = Integer.valueOf(idField.getText());
					if (userID != 0)
						submitButtonPressed(sortPanel, userID);
				}
					
					
			}
		});
		idField.setBounds(132, 25, 158, 25);
		userControls.add(idField);
		
		submitButton = new JButton("Log In");
		submitButton.setBounds(12, 58, 97, 25);
		userControls.add(submitButton);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//extracted this to its own method -j
				if (loggedInAs == 0)
					submitButtonPressed(sortPanel, Integer.valueOf(idField.getText()));
				
			}

			/**
			 * @param panel
			 * @param userID
			 */

		});
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -105, SpringLayout.SOUTH, userControls);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 500, SpringLayout.EAST, sortPanel);
		scrollPane.setBackground(Color.RED);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.EAST, sortPanel);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkOutSelected(e);
			}
		});	
	
		
		// added this here so it populates on startup -j
		refreshDBTable();
		
		try {
			bookshelfPanel = new Bookshelf(cart.toArray(new String[cart.size()]));
			springLayout_1 = (SpringLayout) bookshelfPanel.getLayout();
			springLayout.putConstraint(SpringLayout.WEST, bookshelfPanel, 0, SpringLayout.EAST, scrollPane);
			springLayout.putConstraint(SpringLayout.EAST, bookshelfPanel, -5, SpringLayout.EAST, contentPane);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		springLayout.putConstraint(SpringLayout.NORTH, bookshelfPanel, 5, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.SOUTH, bookshelfPanel, -5, SpringLayout.NORTH, userControls);
		springLayout.putConstraint(SpringLayout.EAST, userControls, -5, SpringLayout.EAST, bookshelfPanel);
		bookshelfPanel.setMinimumSize(new Dimension(150, 10));
		contentPane.add(bookshelfPanel, BorderLayout.EAST);
		
		txtYourBooks = new JTextField();
		txtYourBooks.setText("Your Books:");
		springLayout_1.putConstraint(SpringLayout.NORTH, txtYourBooks, 10, SpringLayout.NORTH, bookshelfPanel);
		springLayout_1.putConstraint(SpringLayout.EAST, txtYourBooks, -10, SpringLayout.EAST, bookshelfPanel);
		bookshelfPanel.add(txtYourBooks);
		txtYourBooks.setColumns(10);
	

	}
	
	/*checks out what's selected in the JTable
	 * 
	 */
	private void checkOutSelected(MouseEvent mouseEvent) {
		
		if (loggedInAs == 0)
		{
			JOptionPane.showMessageDialog(null, "Enter your card number to check out books!");
			return;
		}
		System.out.println(mouseEvent);
		int row = table.getSelectedRow();
		String title = (String) table.getModel().getValueAt(row, 0);
		
		
		if ((int)table.getModel().getValueAt(row, 3) == 0)
		{
			//cart.add(title);
			System.out.println("title from table: " + title);
			try {
				bookshelfPanel.add(title);
				DBManager.updateData("Books", "Title = '" + title + "'", "PatronID = " + loggedInAs);
				bookshelfPanel.drawBooks();
				refreshDBTable();
				bookshelfPanel.repaint();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Checkout Limit Reached!");
			}

		}	
		else
		{
			JOptionPane.showMessageDialog(null, "This book is already checked out!");
		}
	}

	private void refreshDBTable() {
		String sortBy = "";
		String showSortString = " 1=1 ";
		if (radioShowAvail.isSelected())
			showSortString = "  PatronID = 0 ";

		for (JRadioButton b : new JRadioButton[] {radioAuthor, radioTitle, radioGenre})
		{
			if (b.isSelected())
				sortBy = b.getText();
		}
		table.setModel(DbUtils.resultSetToTableModel(DBManager.queryData("Books",sortBy, showSortString,0,"Title", "Author","Genre","PatronID")));
		TableColumnModel col = table.getColumnModel();
		col.removeColumn(col.getColumn(3));  // hide this column from user but leave it in the model because we will access its data
	}
	/**
	 * this method creates the librarian access panel
	 * @param panel
	 * @return
	 */
	private JButton addLibrarianAccess(JPanel panel) {
		JButton librarianAccess = new JButton("Librarian Control Access");
		
		
		librarianAccess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibrarianControl.LaunchLibControl();
			}
		});
		
		librarianAccess.setBounds(12, 359, 179, 36);
		panel.add(librarianAccess);
		panel.repaint();
		return librarianAccess;
	}
	/*
	 * this method triggers when you press the submit button or it is activated remotely
	 */
	private void submitButtonPressed(JPanel panel, int userID) {
		if (loggedInAs != 0)
		{
			System.out.println("pressin log out");
			idField.setVisible(true); 
			lblEnterUserId.setVisible(true);
			setTitle(header);
			loggedInAs = 0;
			submitButton.setText("Log In");	
			try {
				bookshelfPanel = new Bookshelf();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(userID == 34)
				addLibrarianAccess(panel);

			ResultSet rs = DBManager.queryData("Patrons", "PatronID", "PatronID = " + userID, 0, "PatronID","FirstName");
			try {
				rs.next();
				JOptionPane.showMessageDialog(null, "Signed in as: " + rs.getString(2));
				loggedInAs = rs.getInt(1);
				System.out.println("user: " + loggedInAs);
				setTitle(header + " - Welcome " + rs.getString(2) + "!");
				idField.setVisible(false); 
				idField.setText("");
				submitButton.setText("Log Out");
				lblEnterUserId.setVisible(false);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Invalid Card Number! Please try again.");
				return;
			}
			
			//populate bookshelf with user's books
			
			rs = DBManager.queryData("Books", "Title", "PatronID = " + userID, 0, "Title","Author","Genre");
			int count = 0;
			try {
				rs.next();
				while (rs.next())
				{
					count++;
					bookshelfPanel.add(rs.getString(1));
				}
				if (count > 1)
					bookshelfPanel.drawBooks();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error retrieving books! Please try again.");
			}
			
		}
	}	
	private void sortButton(String sortBy) {
		table.setModel(DbUtils.resultSetToTableModel(DBManager.queryData("Books", sortBy, "Title","Author","Genre")));
	}
}
