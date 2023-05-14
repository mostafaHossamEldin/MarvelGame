package exceptions;

public class LeaderAbilityAlreadyUsedException extends GameActionException {
	/*Is thrown when the leader champion of any player
	tries to use his leader ability after it has already been used before. Recall that every leader
	can only use his leader ability once per game.*/
	
	public LeaderAbilityAlreadyUsedException() {
		// TODO Auto-generated constructor stub
	}

	public LeaderAbilityAlreadyUsedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
