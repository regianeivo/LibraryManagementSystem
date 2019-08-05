import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


public class Database {
	// class that will search the user to login into the system
	public static User getUser(int idUser) {
		try {

			// create the connection
			Connection connection = connectDB();

			// Query 
			String cmd = "SELECT * FROM userLibrary where idUser ='" + idUser + "'";

			// Statement
			Statement st = connection.createStatement();

			// Result set that will return the query information
			ResultSet rs = st.executeQuery(cmd);

			// return the object with the table information
			User usu = new User();
			while (rs.next()) {
				usu = new User(rs.getInt("idUser"), rs.getString("nickName"), rs.getString("firstName"),
						rs.getString("LastName"), rs.getBoolean("isAdmin"), rs.getString("pass"));
			}

			// close the connection
			st.close();
			return usu;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return null;
		}
	}

	// class that will search the user that wish to login 
	public static int tryLogIn(String nickName, String pass) {
		try {
			int id = 0;

			Connection connection = connectDB();
			// query that will be executed
			String cmd = "SELECT COALESCE(idUser, 0) As idUser FROM userLibrary " + " where nickName ='" + nickName
					+ "'" + " and pass ='" + pass + "'";

			Statement st = connection.createStatement();

			// result set that will return the information
			ResultSet rs = st.executeQuery(cmd);

			// return the object with the IDUser table information
			while (rs.next()) {
				id = rs.getInt("idUser");
			}
			// close the connection
			st.close();

			if (id > 0) {
				return id;
			} else {
				return 0;
			}
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return 0;
		}
	}

	// this class will treat to insert user to the system
	public static boolean checkNickName(String nickName) {

		int count = 0;
		boolean available = true;
		try {
			// connection to the database
			Connection connection = connectDB();
			
			// statement 
			Statement st = connection.createStatement();

			// ResultSet
			ResultSet resultset = st
					.executeQuery("SELECT COALESCE(COUNT(*), 0) FROM userLibrary where nickName ='" + nickName + "'");
			resultset.next(); // exactly one result so allowed
			count = resultset.getInt(1);

			// validation of the user name against the database table
			if (count > 0) {
				LibraryManagementSystem.print("Name exists");
				available = false;
			}

			// close connection
			st.close();

			return available;

		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}

	// class that will add and update users
	public static boolean updateUser(int idUser, String nickName, String firstName, String lastName, String pass, boolean isAdmin) {

		boolean ret = false;

		try {
			// connection to the database
			Connection connection = connectDB();

			Statement st = connection.createStatement();

			int quantityRec;

			// if user ID does not exist
			if (idUser == 0) {
				quantityRec = st.executeUpdate(
						"insert into userLibrary (nickName, firstName, LastName, isAdmin, pass)" + " values ('"	
				+ nickName + "','" + firstName + "','" + lastName + "'," + isAdmin + ",'" + pass + "')");
			} else {

				quantityRec = st.executeUpdate("update userLibrary " + " set nickName  = '" + nickName + "'"
						+ ", firstName = '" + firstName + "', LastName  = '" + lastName + "', isAdmin   = "
						+ isAdmin + ", pass = '" + pass + "' where idUser    = " + idUser);
			}

			// check the lines affected
			if (quantityRec == 1) {
				ret = true;
			}

			// close connection
			st.close();

			if (!ret) {
				LibraryManagementSystem.print("Error when save User");
			}

			return ret;

		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}

	// search the books by ID
	public static Book getBookByID(int id) {
		try {
			Book book = new Book();

			// open connection
			Connection connection = connectDB();

			Statement st = connection.createStatement();

			String query = "SELECT * FROM book where idBook = " + id;

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				book = new Book(rs.getInt("idBook"), rs.getString("title"), rs.getString("subTitle"),
						rs.getInt("quantity"));
			}
			st.close();
			return book;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return null;
		}
	}
	
	// search the books by Title
	public static Book getBookByTitle(String title) {
		try {
			Book book = new Book();

			// open connection
			Connection connection = connectDB();

			Statement st = connection.createStatement();

			String query = "SELECT * FROM book where title = '" + title + "'";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				book = new Book(rs.getInt("idBook"), rs.getString("title"), rs.getString("subTitle"),
						rs.getInt("quantity"));
			}
			st.close();
			return book;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return null;
		}
	}

	// class that will add and update books
	public static boolean updateBook(int idBook, String titleBook, String subTitleBook, int quantity, int operation) {

		// operation = 0 insert
		// operation = 1 update

		boolean ret = false;
		int resultset;

		try {
			// open connection
			Connection connection = connectDB();

			Statement st = connection.createStatement();

			// will bring sql according with the fields
			if (operation == 0) {
				resultset = st.executeUpdate("insert into book (title, subTitle, quantity) values ('" 
			+ titleBook + "','" + subTitleBook + "'," + quantity + ")");
			} else {
				resultset = st.executeUpdate("update book " + " set title = '" + titleBook + "' , subTitle = '"
						+ subTitleBook + "', quantity = " + quantity + " where idBook = " + idBook);
			}

			// check the lines affected at the database
			if (resultset == 1) {
				ret = true;
			}

			// close connection
			st.close();

			if (!ret) {
				LibraryManagementSystem.print("Error when save BOOK");
			}

			return ret;

		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}

	// class that will delete book by id
	public static boolean deleteBook(int idBook) {

		// typeOp = 0 insert
		// typeOp = 1 update

		boolean ret = false;
		int quantityRec;

		try {
			// open connection
			Connection connection = connectDB();

			Statement st = connection.createStatement();

			quantityRec = st.executeUpdate("delete from book where idBook = " + idBook);

			if (quantityRec == 1) {
				ret = true;
			}

			st.close();

			if (!ret) {
				LibraryManagementSystem.print("Error Delete Book");
			}

			return ret;

		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}


	// class that will add and update books
		public static boolean catchReturnBook(User user, Book book, Boolean isReturn) 
		{

			// operation = 0 insert
			// operation = 1 update

			boolean ret = false;
			int resultset = 0;

			try {
				// open connection
				Connection connection = connectDB();

				Statement st = connection.createStatement();

				// will bring sql according with the fields
				if (isReturn) {
					if (st.executeUpdate("update book set quantity = quantity - 1 where idBook = " + book.getIdBook()) == 1){
						resultset = st.executeUpdate("insert into bookBorrowed (idBook, idUser) values (" + book.getIdBook() + "," + user.getIdUser() + ")");
					}
				} else {
					if (st.executeUpdate("update book set quantity = quantity + 1 where idBook = " + book.getIdBook()) == 1){
						resultset = st.executeUpdate("delete from bookBorrowed where idBook = " + book.getIdBook() + " and idUser = " + user.getIdUser());
					}
				}

				// check the lines affected at the database
				if (resultset == 1) {
					ret = true;
				}

				// close connection
				st.close();

				if (!ret) {
					LibraryManagementSystem.print("Error when save BOOK");
				}

				return ret;

			} catch (Exception e) {
				LibraryManagementSystem.print(e.getMessage());
				return false;
			}
		}
		
		// class that will connect with the database
		public static Connection connectDB() {

			Connection openConnection = null;

			try {
				// mysql driver 
				// In my linux system I used "com.mysql.jdbc.Driver" because the one
				// I left set up on the code was not working
				String driverMySql = "org.gjt.mm.mysql.Driver";

				// string with the way to connect to the database
				String strConn = "jdbc:mysql://localhost:3306/LibraryManagementSystem";

				// will execute the connection using the driver and the string strConn
				// need to change the password
				openConnection = DriverManager.getConnection(strConn, "root", "123");
				Class.forName(driverMySql);

			} catch (SQLException ex) {
				LibraryManagementSystem.print("Connection.SQLERROR: " + ex.getErrorCode());
			} catch (Exception e) {
				LibraryManagementSystem.print(e.getMessage());
			}
			return openConnection;
		}



}
