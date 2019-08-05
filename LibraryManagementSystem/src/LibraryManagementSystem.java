import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Project about Library Management System that allow:
 * - Users register themselves.
 * - Users can rent and return books by themselves.  
 * - Admin can add and remove titles into the stock.
 * - Admin can track the books that are in the stock.
 *  
 * @autor Regiane Ivo(2016126)
 * 
 */


public class LibraryManagementSystem extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {

		mainMenu();
		
	}

	public static void mainMenu() {

		// buttons in the first screen
		Object[] opt = { "Log In", "Sign in", "Exit" };

		do{
		
		// Menu and buttons
		int returnOpt = JOptionPane.showOptionDialog(null, null, "Main Menu", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, opt, null);

		// statement according with the buttons selected
		if (returnOpt == 0) {
			GetLogin();
		} else if (returnOpt == 1) {
			SignIn();
		} else {
			// option to exit the system
			print("Exit");
			break;
		}
		}while(true);
	}

	public static void GetLogin() {
		// instantiate user object
		User user = new User();

		//txtfields to get user nickname and password to login  
		JTextField NickName = new JTextField();
		JTextField pwd = new JPasswordField();

		Object[] msg = { "NickName:", NickName, "Password:", pwd };

		int opt;

		// function to validate against DataBase
		do {
			// call the menu 
			opt = JOptionPane.showConfirmDialog(null, msg, "Login", JOptionPane.OK_CANCEL_OPTION);

			if (opt == JOptionPane.CANCEL_OPTION) {
				break;
			}

			if (NickName.getText().isEmpty() || pwd.getText().isEmpty()) {
				print("Required fields");
			}

		} while (NickName.getText().isEmpty() || pwd.getText().isEmpty());
		
		if (!NickName.getText().isEmpty() && !pwd.getText().isEmpty() && opt == JOptionPane.OK_OPTION) {

			int idUser = Database.tryLogIn(NickName.getText().toString(), pwd.getText().toString());

			if (idUser > 0) {
				user = Database.getUser(idUser);
			} else {
				print("Password invalid!");
			}
		}

		if (opt == JOptionPane.OK_OPTION) {
			// check if the user already exists 
			if (user != new User()) {
				// in case the user exists, check the password
				print("Successful");
				// check the menu according with the user, if it is admin or no
				if (user.GetIsAdmin()) {
					internalMenu(user);

				} else {
					internalMenu(user);
				}

			} else {
				print("Failed"); // error 
									
			}
		} else {
			print("Canceled"); // cancel operation
									
		}
	}

	// class that will show the menu to add new user 
	public static void SignIn() {
		int JOption;
		// necessary fields to add a new user
		JTextField nickName = new JTextField();
		JTextField firstName = new JTextField();
		JTextField lastName = new JTextField();
		JTextField pwd = new JPasswordField();
		JTextField pwdConfirm = new JPasswordField();
		JCheckBox checkbox = new JCheckBox("Admin User?");

		// object that will show at the screen
		Object[] message = { "NickName:", nickName, "First Name:", firstName, "Last Name:", lastName, "Password:", pwd,
				"Password Confirmation:", pwdConfirm, checkbox };

		// buttons to SAVE and CANCEL
		Object[] options = { "SAVE", // 0
				"CANCEL" }; // 1
		do {
			// Menu
			JOption = JOptionPane.showOptionDialog(null, message, "SignIn", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			// in case the user cancel the operation
			if (JOption == 1) {
				break;
			}

			if (!pwd.getText().equals(pwdConfirm.getText())) {
				print("Passwords don't match");
			}
			
		} while (nickName.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty()
				|| pwd.getText().isEmpty() || !pwd.getText().equals(pwdConfirm.getText()) || pwd.getText().isEmpty() || !Database.checkNickName(nickName.getText().toString()));

		// in case the user confirm the operation
		if (JOption == 0) {

			// call the class DB to insert new user
			// in case it does not happen, will be threaten inside the class DB
			User usu = new User(0, nickName.getText().toString(), firstName.getText().toString(),
					lastName.getText().toString(), checkbox.isSelected(), pwd.getText().toString());

			if (usu.updateUser()) {
				print("Saved");

				if (usu.GetIsAdmin()) {
					internalMenu(usu);

				} else {
					internalMenu(usu);
				}

			}
		} else {
			print("Canceled");// when the user cancel the operation
		}
	}

	// class that will give the options to the admin 

	public static void internalMenu(User user) {
		// buttons
		int JOption;
		int posExit;

		do {
			
			user = Database.getUser(user.getIdUser());
			
			// List of books
			List<Book> books = new ArrayList<Book>();
			
			// List that will show at the screen
			JComboBox<String> boxBooks = new JComboBox<String>();
			
			// Return all books
			books = user.listBook(0);

			for (Book book : books) {
				boxBooks.addItem(book.getTitleBook());
			}
			
			Object[] message = { user.getFirstName() + "\nList of Books", boxBooks,
					"______________________________________________________________________\n" };

			if (user.GetIsAdmin()) {
				posExit = 4;
				
				Object[] optionsEvent = { "Edit Profile", // 0
						"Add BOOK", // 1
						"Edit BOOK", // 2
						"Remove BOOK", // 3
						"Logout" }; // 4

				// menu to input information and buttons
				JOption = JOptionPane.showOptionDialog(null, message, "Internal Menu", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, optionsEvent, optionsEvent[0]);
			} else {
				posExit = 3;
				Object[] optionsEvent = { "Edit Profile", // 0
						"Borrow BOOK", // 1
						"Return BOOK", // 2
						"Logout" }; // 3
				// menu to input information and buttons
				JOption = JOptionPane.showOptionDialog(null, message, "Internal Menu", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, optionsEvent, optionsEvent[0]);
			}

			// check the choices and call the correct class
			if (user.GetIsAdmin()) {

				if (JOption == 0) {
					EditUser(user);
				} else if (JOption == 1) {
					if (addBook()) {
						print("Book saved");
					}
				} else if (JOption == 2) {
					editBook(boxBooks.getSelectedItem().toString());
				} else if (JOption == 3) {
					removeBook(boxBooks.getSelectedItem().toString());
				} else {
					print("Logout");
				}

			} else {
				if (JOption == 0) {
					EditUser(user);
				} else if (JOption == 1) {
					rentBook(user);
				} else if (JOption == 2) {
					ReturnBook(user);
				} else {
					print("Logout");
				}
			}
		} while (JOption != posExit);
	}

	
	public static void EditUser(User user) {
		
		int JOption;
		// fields
		JTextField firstName = new JTextField();
		JTextField lastName = new JTextField();
		JTextField pwd = new JPasswordField();
		JTextField pwdConfirm = new JPasswordField();
		JCheckBox checkbox = new JCheckBox("Admin User?");
		
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		pwd.setText(user.getPassword());
		pwdConfirm.setText(user.getPassword());
		
		checkbox.setSelected(user.GetIsAdmin());

		Object[] message = { "NickName:", user.getNickName(), "First Name:", firstName, "Last Name:", lastName, "Password:", pwd,
				"Password Confirmation:", pwdConfirm, checkbox };

		// buttons
		Object[] options = { "SAVE", // 0
				"CANCEL" }; // 1
		do {
			// menu to input informations / buttons with options
			JOption = JOptionPane.showOptionDialog(null, message, "SignIn", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			// cancel the operation
			if (JOption == 1) {
				break;
			}

			if (!pwd.getText().equals(pwdConfirm.getText())) {
				print("Passwords don't match");
			}
			// in case the menu be empty
		} while (firstName.getText().isEmpty() || lastName.getText().isEmpty()
				|| pwd.getText().isEmpty() || !pwd.getText().equals(pwdConfirm.getText()) || pwd.getText().isEmpty());

		// in case click in "confirm!"
		if (JOption == 0) {

			// class the class DB to insert new user
			User usu = new User(user.getIdUser(), user.getNickName(), firstName.getText().toString(),
					lastName.getText().toString(), checkbox.isSelected(), pwd.getText().toString());

			if (usu.updateUser()) {
				print("Saved");
			}
		} else {
			print("Canceled");// in case user cancel the operation
		}
	}
	
	
	// class that will add new books
	public static boolean addBook() {

		boolean ret = false;
		int JOption;
		// fields
		JTextField title = new JTextField();
		JTextField subTitle = new JTextField();
		JTextField quantity = new JTextField();

		Object[] message = { "\nTitle:", title, "\nSubTitle:", subTitle, "\nStock Quantity:", quantity, " \n\n" };
		// buttons
		Object[] options = { "ADD BOOK", // 0
				"CANCEL" }; // 1

		quantity.setText("0");

		do {
			// menu to input informations, buttons
			JOption = JOptionPane.showOptionDialog(null, message, "          *** Add Book ***",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

			// cancel option 
			if (JOption == 1) {
				break;
			}

			if (!quantity.getText().matches("[0-9]+")) {
				print("Only numbers in stock");
			}

			// in case fields empty, show the msg and come back to menu
			if (title.getText().isEmpty() || subTitle.getText().isEmpty()) {
				print("Required field");
			}

		} while (title.getText().isEmpty() || subTitle.getText().isEmpty()); 
																			
		// keep the menu by the book have title
		if (JOption == 0) {
			// creation of the book inside DB
			ret = Database.updateBook(0, title.getText().toString(), subTitle.getText().toString(), Integer.parseInt(quantity.getText().toString()), 0);
		}

		return ret;
	}

	// class that will update books
	public static void editBook(String title) {

		int JOption;

		Book book = new Book();

		book = Database.getBookByTitle(title);

		// fields
		JTextField Jtitle = new JTextField();
		JTextField subTitle = new JTextField();
		JTextField quantity = new JTextField();

		Jtitle.setText(book.getTitleBook());
		subTitle.setText(book.getSubTitleBook());
		quantity.setText(String.valueOf(book.getQuantity()));

		try {

			boolean isInt;

			do {

				Object[] message = { "\n\nTitle:", Jtitle, "\nSubTitle:", subTitle, "\nStock Quantity:", quantity,
						" \n\n" };
				// buttons
				Object[] options = { "SAVE BOOK", // 0
						"CANCEL" }; // 1

				isInt = false;
				// menu to input information and buttons
				JOption = JOptionPane.showOptionDialog(null, message, "Edit Book", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);

				if (JOption == 1) {
					break;
				}

				if (!quantity.getText().matches("[0-9]+")) {
					print("Only numbers in stock");
				} else {
					isInt = true;
				}

				// in case the fields be empty, show the msg and come back to the menu
				if (Jtitle.getText().isEmpty() || subTitle.getText().isEmpty() || !isInt) {
					print("Required field");
				}

			// will keep the menu by the Title field be filled
			} while (Jtitle.getText().isEmpty() || subTitle.getText().isEmpty()); 

			
			// Save option
			if (JOption == 0) {
				Database.updateBook(book.getIdBook(), Jtitle.getText().toString(), subTitle.getText().toString(),
						Integer.parseInt(quantity.getText().toString()), 2);
			}

		} catch (Exception e) {
			print(e.getMessage());
		}
	}

	// class similar with update, the difference that will remove the book 
	
	public static void removeBook(String nameBook) {

		int n;
		Book book = new Book();

		try { 

			book = Database.getBookByTitle(nameBook);

			Object[] message = { "\nAre you sure you want to remove the book? \n\n                     Title: "
					+ book.getTitleBook() + "\n\n" };

			Object[] options = { "Delete", // 0
					"Cancel" }; // 1 
			n =	JOptionPane.showOptionDialog(null, message, "Remove Book", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

			// if the user confirm remove, will call the class delete book

			if (n == 0) {

				if(Database.deleteBook(book.getIdBook())){
					print("Book deleted");
				}
			}

		} catch (Exception e) {
			print(e.getMessage());
		}
	}

	// class that will give the books available to be borrowed  
	public static void rentBook(User user) {

		List<Book> books = new ArrayList<Book>();
		Book rentedBook = new Book();
		JComboBox<String> booksForRent = new JComboBox<String>();

		Object[] optionsEvent = { "Rent BOOK", // 0
				"Cancel" }; // 1

		// search the book in the DB
		books = user.listBook(1);

		// book list
		for (Book book : books) {
			booksForRent.addItem(book.getTitleBook());
		}

		Object[] message = { "List Of Available Books", booksForRent };

		// menu and buttons
		int JOption = JOptionPane.showOptionDialog(null, message, "Rent", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, optionsEvent, optionsEvent[0]);

		// validate option and class the class 
		if (JOption == 0) {

			rentedBook = Database.getBookByTitle(booksForRent.getSelectedItem().toString());

			if (user.catchReturnBook(user, rentedBook, true)) {
				print("Book Borrowed");
			}
		}
	}

	// class that will show the borrowed books that the user will return
	public static void ReturnBook(User user) {

		List<Book> books = new ArrayList<Book>();
		Book bookReturned = new Book();
		JComboBox<String> booksForReturn = new JComboBox<String>();

		// search the books borrowed according with the logged user 
		books = user.listBook(3);

		// books list
		for (Book book : books) {
			booksForReturn.addItem(book.getTitleBook());
		}

		Object[] optionsEvent = { "Return", // 0
				"Cancel" }; // 1

		Object[] message = { "List Of Available Books", booksForReturn };

		// menu and buttons
		int JOption = JOptionPane.showOptionDialog(null, message, "Return Book",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionsEvent, optionsEvent[0]);

		// validate option and class designated class
		if (JOption == 0) {
			
			bookReturned = Database.getBookByTitle(booksForReturn.getSelectedItem().toString());

			if (user.catchReturnBook(user, bookReturned, false)) {
				print("Book Returned");
			}
		}

	}

	// class that will receive String to show at the screen
	// very fast
	public static void print(String message) {

		final JOptionPane JOption = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
				null, new Object[] {}, null);

		final JDialog modal = new JDialog();
		modal.setTitle(null);
		modal.setModal(true);

		modal.setContentPane(JOption);

		modal.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		modal.pack();

		modal.setLocationRelativeTo(null);

		Timer time = new Timer(800, new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				modal.dispose();
			}
		});
		time.setRepeats(false);

		time.start();

		modal.setVisible(true);
	}

}