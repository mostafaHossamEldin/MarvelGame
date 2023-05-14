package exceptions;

public class InvalidTargetException extends GameActionException{
	/* A subclass of GameActionException representing an exception that is thrown when a
	champion tries to cast an ability on an invalid target or an empty cell.*/
	
	public InvalidTargetException() {
		super();
	}
	public InvalidTargetException(String s) {
		super(s);
	}

}
