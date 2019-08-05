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


//class Book with fields, constructor, getters and setters and methods
public class Book {
	//fields
	private int idBook;
    private String titleBook;
    private String subTitleBook;
    private int quantity;
    
    //empty constructor
    public Book(){

    }

    //constructor
    public Book (int idBook, String titleBook, String subTitleBook, int quantity){
        this.setIdBook(idBook);
        this.setTitleBook(titleBook);
        this.setSubTitleBook(subTitleBook);
        this.setQuantity(quantity);
    }

    //getters and setters
	public int getIdBook() {
		return idBook;
	}

	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}

	public String getTitleBook() {
		return titleBook;
	}

	public void setTitleBook(String titleBook) {
		this.titleBook = titleBook;
	}

	public String getSubTitleBook() {
		return subTitleBook;
	}

	public void setSubTitleBook(String subTitleBook) {
		this.subTitleBook = subTitleBook;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void sumQuantity(){
		this.quantity = this.quantity +1;
	}
	
	public void subtractQuantity(){
		this.quantity = this.quantity -1;
	}
	
	public boolean updateBook(int operation) {
		boolean ret = false;
		try {
			ret = Database.updateBook(getIdBook(), getTitleBook(), getSubTitleBook(), getQuantity(), operation);			
			return ret;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return false;
		}
	}
	
	public Book getBookByID(int operation) {
		Book book = new Book();
		try {
			book = Database.getBookByID(getIdBook());			
			return book;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return book;
		}
	}
	
	public Book getBookByTitle(int operation) {
		Book book = new Book();
		try {
			book = Database.getBookByTitle(getTitleBook());			
			return book;
		} catch (Exception e) {
			LibraryManagementSystem.print(e.getMessage());
			return book;
		}
	}
	
	
}
