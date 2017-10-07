package library;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class LibrarianControl extends JFrame {

	private JPanel contentPane;
	private JTable bookTable;
	private JTable userTable;
	private Vector selectedCells = new Vector<int[]>();
	private boolean pressingCTRL=false;

	/**
	 * Launch the application.
	 */
	public static void LaunchLibControl() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibrarianControl frame = new LibrarianControl();
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
	public LibrarianControl() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 978, 503);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane_Books = new JScrollPane();
		scrollPane_Books.setBounds(5, 5, 398, 256);
		contentPane.add(scrollPane_Books);
		
		bookTable = new JTable();
		ResultSet results = DBUtils.queryData("Books", "BookID", "BookID", "Title", "Author", "Genre");
		bookTable.setModel(DBUtils.resultSetToTableModel(results));
		scrollPane_Books.setViewportView(bookTable);
		bookTable.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane_Users = new JScrollPane();
		scrollPane_Users.setBounds(503, 0, 426, 261);
		contentPane.add(scrollPane_Users);
		
		userTable = new JTable();
		scrollPane_Users.setViewportView(userTable);
		ResultSet resultsP = DBUtils.queryData("Patrons", "PatronID", "PatronID", "FirstName", "LastName");
		userTable.setModel(DBUtils.resultSetToTableModel(resultsP));
		scrollPane_Users.setViewportView(userTable);
		userTable.getTableHeader().setReorderingAllowed(false);
		
		
		JTextField fNameField = new JTextField();
		JTextField lNameField = new JTextField();
		
		
		//Add Patron Button
		JButton btnAddPatron = new JButton("Add Patron");
		btnAddPatron.setEnabled(false);
		btnAddPatron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					DBUtils.addPatron(fNameField.getText(), lNameField.getText());
					JOptionPane.showMessageDialog(null, "Successfully added " + fNameField.getText() + " " + lNameField.getText());
					fNameField.setText("");
					lNameField.setText("");
					
					
					btnAddPatron.setEnabled(false);
					ResultSet results = DBUtils.queryData("Patrons", "PatronID", "PatronID", "FirstName", "LastName");
					userTable.setModel(DBUtils.resultSetToTableModel(results));
					
					}
					catch (Exception E){
						System.out.println(E.getMessage());
						btnAddPatron.setEnabled(false);
						JOptionPane.showMessageDialog(null, "Did not successfully add book.");
					}
				
			}
		});
		btnAddPatron.setBounds(818, 272, 111, 65);
		contentPane.add(btnAddPatron);
		
		
		
		
		
		//Romove Patron Button
		JButton btnRemovePatron = new JButton("Remove Selected Patron");
		btnRemovePatron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = 0;
				int[] selectedRows = userTable.getSelectedRows();
				List<Integer> values = new ArrayList<>();
				for(int el : selectedRows) {
					values.add((int) userTable.getModel().getValueAt(el, 0));
					System.out.println(values.toString());
				}
				for(int el : values) {
					DBUtils.removeByID("Patrons", el);
					count++;
				}
				ResultSet results = DBUtils.queryData("Patrons", "PatronID", "PatronID", "FirstName", "LastName");
				userTable.setModel(DBUtils.resultSetToTableModel(results));
				String bookS = (count>1) ? "users" : "user";
				JOptionPane.showMessageDialog(null, "Removed " + count + " " + bookS + " from database.");
			}
		});
		btnRemovePatron.setBounds(778, 361, 174, 51);
		contentPane.add(btnRemovePatron);
		
		JPanel bookPanel = new JPanel();
		bookPanel.setBounds(5, 273, 237, 146);
		contentPane.add(bookPanel);
		bookPanel.setLayout(null);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(194, 31, 46, 14);
		bookPanel.add(lblTitle);
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setBounds(194, 63, 46, 14);
		bookPanel.add(lblAuthor);
		
		JLabel lblGenre = new JLabel("Genre");
		lblGenre.setBounds(194, 96, 46, 14);
		bookPanel.add(lblGenre);
		
		JTextField genreField = new JTextField("");
		genreField.setBounds(10, 92, 170, 22);
		bookPanel.add(genreField);
		
		JTextField authorField = new JTextField("");
		authorField.setBounds(10, 59, 170, 22);
		bookPanel.add(authorField);
		
		JTextField titleField = new JTextField("");
		titleField.setBounds(10, 27, 170, 22);
		bookPanel.add(titleField);
		bookPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{authorField, titleField, genreField}));
		
		JButton btnAddBook = new JButton("Add Book");
		btnAddBook.setBounds(292, 272, 111, 65);
		contentPane.add(btnAddBook);
		btnAddBook.setEnabled(false);
		
		
		/*
		 * Testing key listeners for all fields-- WORKS!!
		 */
		titleField.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            super.keyReleased(e);
	            if(titleField.getText().length() > 0 && authorField.getText().length() > 0 && genreField.getText().length() > 0)
	                btnAddBook.setEnabled(true);
	            else
	                btnAddBook.setEnabled(false);
	        }
	    });
		
		authorField.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            super.keyReleased(e);
	            if(titleField.getText().length() > 0 && authorField.getText().length() > 0 && genreField.getText().length() > 0)
	                btnAddBook.setEnabled(true);
	            else
	                btnAddBook.setEnabled(false);
	        }
	    });
		
		genreField.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            super.keyReleased(e);
	            if(titleField.getText().length() > 0 && authorField.getText().length() > 0 && genreField.getText().length() > 0)
	                btnAddBook.setEnabled(true);
	            else
	                btnAddBook.setEnabled(false);
	        }
	    });
		
		//Remove Selected Book button
		JButton btnRemoveBook = new JButton("Remove Selected Book");
		btnRemoveBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int count = 0;
				int[] selectedRows = bookTable.getSelectedRows();
				List<Integer> values = new ArrayList<>();
				for(int el : selectedRows) {
					values.add((int) bookTable.getModel().getValueAt(el, 0));
					System.out.println(values.toString());
				}
				for(int el : values) {
					DBUtils.removeByID("Books", el);
					count++;
				}
				ResultSet results = DBUtils.queryData("Books", "BookID", "BookID", "Title", "Author", "Genre");
				bookTable.setModel(DBUtils.resultSetToTableModel(results));
				String bookS = (count>1) ? "books" : "book";
				JOptionPane.showMessageDialog(null, "Removed " + count + " " + bookS + " from database.");
			}
		});
		
		
		btnRemoveBook.setBounds(252, 352, 157, 51);
		contentPane.add(btnRemoveBook);
		
		JPanel patronPanel = new JPanel();
		patronPanel.setBounds(463, 272, 310, 96);
		contentPane.add(patronPanel);
		patronPanel.setLayout(null);
		
		
		fNameField.setBounds(31, 24, 194, 22);
		patronPanel.add(fNameField);
		
		
		lNameField.setBounds(31, 63, 194, 22);
		patronPanel.add(lNameField);
		
		/*
		 * Listeners for lastNameField and fNameField
		 */
		fNameField.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            super.keyReleased(e);
	            if(fNameField.getText().length() > 0 && lNameField.getText().length() > 0)
	                btnAddPatron.setEnabled(true);
	            else
	                btnAddPatron.setEnabled(false);
	        }
	    });
		
		lNameField.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            super.keyReleased(e);
	            if(fNameField.getText().length() > 0 && lNameField.getText().length() > 0)
	                btnAddPatron.setEnabled(true);
	            else
	                btnAddPatron.setEnabled(false);
	        }
	    });
		
		
		JLabel lblLastname = new JLabel("LastName");
		lblLastname.setBounds(235, 67, 59, 14);
		patronPanel.add(lblLastname);
		
		JLabel lblFirstname = new JLabel("FirstName");
		lblFirstname.setBounds(235, 28, 77, 14);
		patronPanel.add(lblFirstname);
		patronPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{fNameField, lNameField}));
		
		//Add book listener
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
				DBUtils.addBook(titleField.getText(), authorField.getText(), genreField.getText());
				JOptionPane.showMessageDialog(null, "Successfully added " + titleField.getText());
				titleField.setText("");
				authorField.setText("");
				genreField.setText("");
				
				btnAddBook.setEnabled(false);
				ResultSet results = DBUtils.queryData("Books", "BookID", "BookID", "Title", "Author", "Genre");
				bookTable.setModel(DBUtils.resultSetToTableModel(results));
				
				}
				catch (Exception e){
					System.out.println(e.getMessage());
					btnAddBook.setEnabled(false);
					JOptionPane.showMessageDialog(null, "Did not successfully add book.");
				}
	
			}
		});
			
	}
}
