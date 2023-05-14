package exceptions;

public class NotEnoughResourcesException extends GameActionException {
	//Is thrown when trying to perform any action without having enough resource(s) for this action.

	public NotEnoughResourcesException() {
		// TODO Auto-generated constructor stub
	}

	public NotEnoughResourcesException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
