package library;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(225, 100));
		contentPane.add(panel, BorderLayout.WEST);
		
		JLabel btnShowBooks = new JLabel("Show Books");
		btnShowBooks.setBounds(12, 5, 150, 30);
		btnShowBooks.setPreferredSize(new Dimension(150, 30));
		panel.setLayout(null);
		panel.add(btnShowBooks);
		
		JRadioButton chckbxShowAllBooks = new JRadioButton("Show All Books");
		chckbxShowAllBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				table.setModel(DbUtils.resultSetToTableModel(DBManager.queryData("Books", "Title", "Title", "Author", "Genre")));
			}
		});
		bookDisplaybuttonGroup.add(chckbxShowAllBooks);
		chckbxShowAllBooks.setBounds(12, 46, 115, 25);
		chckbxShowAllBooks.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chckbxShowAllBooks);
		
		JRadioButton chckbxShowAvailableBooks = new JRadioButton("Show Available Books");
		bookDisplaybuttonGroup.add(chckbxShowAvailableBooks);
		chckbxShowAvailableBooks.setBounds(12, 76, 153, 25);
		panel.add(chckbxShowAvailableBooks);
		
		JLabel btnNewButton_1 = new JLabel("Sort Books");
		btnNewButton_1.setBounds(12, 146, 150, 30);
		btnNewButton_1.setPreferredSize(new Dimension(150, 30));
		panel.add(btnNewButton_1);
		
		JRadioButton chckbxAuthorAz = new JRadioButton("Author A-Z");
		chckbxAuthorAz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sortButton("Author");
			}
		});
		sortButtonGroup.add(chckbxAuthorAz);
		chckbxAuthorAz.setBounds(12, 204, 91, 25);
		panel.add(chckbxAuthorAz);
		
		JRadioButton chckbxBookTitleAz = new JRadioButton("Book Title A-Z");
		chckbxBookTitleAz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sortButton("Title");
			}
		});
		sortButtonGroup.add(chckbxBookTitleAz);
		chckbxBookTitleAz.setBounds(12, 233, 113, 25);
		panel.add(chckbxBookTitleAz);
		
		JRadioButton chckbxGenre = new JRadioButton("Genre");
		chckbxGenre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sortButton("Genre");
			}
		});
		sortButtonGroup.add(chckbxGenre);
		chckbxGenre.setBounds(12, 261, 113, 25);
		panel.add(chckbxGenre);
		
		// extracted the librarian access button to a method -j
		
		JPanel panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(100, 100));
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(null);
		
		lblEnterUserId = new JLabel("Enter User ID: ");
		lblEnterUserId.setBounds(12, 29, 85, 16);
		panel_2.add(lblEnterUserId);
		
		// -j
		idField = new JTextField();
		idField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				if (k.getKeyCode() == 10) // enter
				{
					String userID = idField.getText();
					if (userID != null && !userID.equals(""))
						submitButtonPressed(panel, userID);
				}
					
					
			}
		});
		idField.setBounds(97, 25, 158, 25);
		panel_2.add(idField);
		
		submitButton = new JButton("Log In");
		submitButton.setBounds(12, 58, 97, 25);
		panel_2.add(submitButton);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//extracted this to its own method -j
				submitButtonPressed(panel, idField.getText());
				
			}

			/**
			 * @param panel
			 * @param userID
			 */

		});
		
		JButton btnCart = new JButton("Go To Cart");
		btnCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//CartWindow.LaunchCartScreen();
				// for testing only, since i had it already made:
				System.out.println(cart.toString());
				Bookshelf.Launch(cart.toArray(new String[cart.size()]));
				
			}
		});
		btnCart.setBounds(828, 25, 170, 58);
		panel_2.add(btnCart);
		
		JButton btnCheckOut = new JButton("Add to Cart");
		btnCheckOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent k) {
				System.out.println(k);
				int row = table.getSelectedRow();
				String title = (String) table.getModel().getValueAt(row, 0);
				cart.add(title);
				DBManager.updateData("Books", "Title = '" + title + "'", "CheckedOut = '2012-4-5'");
			}
		}); 
		btnCheckOut.setBounds(626, 26, 170, 58);
		panel_2.add(btnCheckOut);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, 500));
		contentPane.add(scrollPane, BorderLayout.EAST);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
	
		
		// added this here so it populates on startup -j
		table.setModel(DbUtils.resultSetToTableModel(DBManager.queryData("Books", "Title", "Title", "Author", "Genre", "CheckedOut")));
		
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
	private void submitButtonPressed(JPanel panel, String userID) {
		if (submitButton.getText().equals("Log Out"))
		{
			idField.setVisible(true); 
			lblEnterUserId.setVisible(true);
			setTitle(header);
			submitButton.setText("Log In");	
		}
		else
		{
			// select from patrons where isLibrarian = true
			// TODO implement this query when wueryData can do WHERE statements
			if(userID.equals("Bob"))
				addLibrarianAccess(panel);

			JOptionPane.showMessageDialog(null, "Signed in as: " + userID);
			setTitle(header + " - Welcome " + userID + "!");
			idField.setVisible(false); 
			idField.setText("");
			submitButton.setText("Log Out");
			lblEnterUserId.setVisible(false);
		}
	}	
	private void sortButton(String sortBy) {
		table.setModel(DbUtils.resultSetToTableModel(DBManager.queryData("Books", sortBy, "Title","Author","Genre")));
	}
}
