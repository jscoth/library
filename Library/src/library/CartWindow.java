package library;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class CartWindow extends JFrame {

	private JPanel contentPane;
	private JTable inCartTable;
	private JTable checkedOutTable;

	/**
	 * Launch the application.
	 */
	public static void LaunchCartScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CartWindow frame = new CartWindow();
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
	public CartWindow() {
		setTitle("Your Books");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 711, 479);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane checkOutPane = new JScrollPane();
		checkOutPane.setBounds(354, 13, 327, 305);
		contentPane.add(checkOutPane);
		
		inCartTable = new JTable();
		checkOutPane.setViewportView(inCartTable);
		
		JScrollPane returnPane = new JScrollPane();
		returnPane.setBounds(10, 13, 316, 306);
		contentPane.add(returnPane);
		
		checkedOutTable = new JTable();
		returnPane.setViewportView(checkedOutTable);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(12, 327, 669, 81);
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("Return Selected");
		btnNewButton.setBounds(0, 0, 164, 81);
		buttonPanel.add(btnNewButton);
		
		JButton btnCheckOut = new JButton("Check Out Selected");
		btnCheckOut.setBounds(492, 0, 177, 81);
		buttonPanel.add(btnCheckOut);
	}
}
