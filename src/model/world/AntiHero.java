package model.world;

import java.util.ArrayList;

import engine.Game;
import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion champion : targets) {
			Stun effect = new Stun(2);
			champion.getAppliedEffects().add(effect);
			effect.apply(champion);
		}
	}

}
