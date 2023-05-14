package model.world;

import java.util.ArrayList;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion champion : targets) {
			champion.setCurrentHP(0);
			champion.setCondition(Condition.KNOCKEDOUT);
		}
	}

}
