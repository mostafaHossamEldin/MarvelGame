package exceptions;

public class UnallowedMovementException extends GameActionException {
	//Is thrown when a champion is trying to move while violating the move regulations.
	
	public UnallowedMovementException() {
		// TODO Auto-generated constructor stub
	}

	public UnallowedMovementException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
