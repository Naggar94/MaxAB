package returnables;

public class Error {
	String message;
	Boolean is_error;
	
	public Error(String message) {
		// TODO Auto-generated constructor stub
		is_error = true;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Boolean getIs_error() {
		return is_error;
	}
}
