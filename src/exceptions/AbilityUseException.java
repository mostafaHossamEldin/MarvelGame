package exceptions;

public class AbilityUseException extends GameActionException {
	/* Is thrown when a champion is trying to cast an ability on an out
	of range target or being in a condition that prevents him from casting an ability.*/
	
	public AbilityUseException() {
		// TODO Auto-generated constructor stub
	}

	public AbilityUseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
