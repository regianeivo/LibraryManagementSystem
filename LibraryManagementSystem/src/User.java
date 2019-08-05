import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

public class User {
	private int idUser;
	private String nickName;
	private String firstName;
	private String lastName;
	private Boolean isAdmin;
	private String password;

	//empty constructor
	public User() {

	}

	//constructor
	public User(int idUser, String nickName, String firstName, String lastName, Boolean isAdmin, String password) {
		this.idUser = idUser;
		this.nickName = nickName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isAdmin = isAdmin;
		this.password = password;
	}

	//getters and setters
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean GetIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean updateUser() {
		boolean ret = false;
		try {
			ret = Database.updateUser(getIdUser(), getNickName(), getFirstName(), getLastName(), getPassword(), GetIsAdmin());			
			return ret;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}
	
	public boolean catchReturnBook(User user, Book book, Boolean isRent) {
		boolean ret = false;
		try {
			ret = Database.catchReturnBook(user, book, isRent);			
			return ret;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}
	
	//will search the books using the fields
		public List<Book> listBook(int typeSearch) {

			//Array that will bring the results of the search
			List<Book> books = new ArrayList<Book>();
			
			//search for all books at the database
			String sql = "SELECT idBook FROM book b";

			//statement that return books in stock or not and the borrowed ones 
			if (typeSearch == 1) {
				sql = sql + " where quantity > 0";
			} else if (typeSearch == 2) { 
				sql = sql + " where quantity <= 0";
			} else if (typeSearch == 3){ 
				sql = sql + " where exists (select 1 from bookBorrowed bb where bb.idBook = b.idBook and bb.idUser = " + getIdUser() + ")";
			}

			try {

				//open connection
				Connection connection = Database.connectDB();

				//statement 
				Statement st = connection.createStatement();

				//ResultSet
				ResultSet rs = st.executeQuery(sql);

				//will bring the list of books 
				while (rs.next()) {

					Book book = Database.getBookByID(rs.getInt("idBook"));

					//add object in the list
					books.add(book);
				}
				//close connection
				st.close();

				return books;

			} catch (Exception e) {
				LibraryManagementSystem.print(e.getMessage());
				return null;
			}
		}
}
