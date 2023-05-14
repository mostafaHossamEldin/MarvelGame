package exceptions;

public class ChampionDisarmedException extends GameActionException{
	/* A subclass of GameActionException representing an exception that is thrown when a
	champion tries to use a normal attack while being disarmed.*/

	public ChampionDisarmedException() {
		super();
	}
	
	public ChampionDisarmedException(String s) {
		super(s);
	}

}
