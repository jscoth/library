package library;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class LibrarianControl extends JFrame {

	private JPanel contentPane;
	private JTable bookTable;
	private JTable userTable;

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
		ResultSet results = DBManager.queryData("Books", "BookID", "BookID", "Title", "Author", "Genre");
		bookTable.setModel(DbUtils.resultSetToTableModel(results));
		scrollPane_Books.setViewportView(bookTable);
		
		JScrollPane scrollPane_Users = new JScrollPane();
		scrollPane_Users.setBounds(503, 0, 426, 261);
		contentPane.add(scrollPane_Users);
		
		userTable = new JTable();
		scrollPane_Users.setViewportView(userTable);
		ResultSet resultsP = DBManager.queryData("Patrons", "PatronID", "PatronID", "FirstName", "LastName");
		userTable.setModel(DbUtils.resultSetToTableModel(resultsP));
		scrollPane_Users.setViewportView(userTable);
		
		JTextField titleField = new JTextField("");
		titleField.setBounds(12, 297, 170, 22);
		contentPane.add(titleField);
		
		JTextField authorField = new JTextField("");
		authorField.setBounds(12, 332, 170, 22);
		contentPane.add(authorField);
		
		JTextField genreField = new JTextField("");
		genreField.setBounds(12, 367, 170, 22);
		contentPane.add(genreField);
		
		JButton btnAddBook = new JButton("Add Book");
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
		
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
				DBManager.addBook(titleField.getText(), authorField.getText(), genreField.getText());
				JOptionPane.showMessageDialog(null, "Successfully added " + titleField.getText());
				titleField.setText("");
				authorField.setText("");
				genreField.setText("");
				
				btnAddBook.setEnabled(false);
				ResultSet results = DBManager.queryData("Books", "BookID", "BookID", "Title", "Author", "Genre");
				bookTable.setModel(DbUtils.resultSetToTableModel(results));
				
				}
				catch (Exception e){
					System.out.println(e.getMessage());
					btnAddBook.setEnabled(false);
					JOptionPane.showMessageDialog(null, "Did not successfully add book.");
				}
	
			}
		});
		btnAddBook.setBounds(306, 279, 97, 58);
		contentPane.add(btnAddBook);
		
		JButton btnRemoveBook = new JButton("Remove Selected Book");
		btnRemoveBook.setBounds(264, 353, 157, 51);
		contentPane.add(btnRemoveBook);
		
		JTextField fNameField = new JTextField();
		fNameField.setBounds(509, 297, 221, 22);
		contentPane.add(fNameField);
		
		JTextField lastNameField = new JTextField();
		lastNameField.setBounds(509, 332, 221, 22);
		contentPane.add(lastNameField);
		
		JButton btnAddPatron = new JButton("Add Patron");
		btnAddPatron.setBounds(842, 272, 87, 65);
		contentPane.add(btnAddPatron);
		
		JButton btnRemovePatron = new JButton("Remove Selected Patron");
		btnRemovePatron.setBounds(755, 368, 174, 51);
		contentPane.add(btnRemovePatron);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(198, 301, 46, 14);
		contentPane.add(lblTitle);
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setBounds(198, 336, 46, 14);
		contentPane.add(lblAuthor);
		
		JLabel lblGenre = new JLabel("Genre");
		lblGenre.setBounds(198, 371, 46, 14);
		contentPane.add(lblGenre);
		
		JLabel lblFirstname = new JLabel("FirstName");
		lblFirstname.setBounds(755, 301, 77, 14);
		contentPane.add(lblFirstname);
		
		JLabel lblLastname = new JLabel("LastName");
		lblLastname.setBounds(755, 336, 59, 14);
		contentPane.add(lblLastname);
	}
}
