package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion champion : targets) {
			ArrayList<Effect> effectsToBeRemovedCauseTheyAreDebuffsSoWeNeedToRemoveThem = new ArrayList<Effect>();
			int x = 0;
			for (Effect eff : champion.getAppliedEffects()) {
				if ( eff.getType() == EffectType.DEBUFF) {
					effectsToBeRemovedCauseTheyAreDebuffsSoWeNeedToRemoveThem.add(eff);
					x++;
				}
			}
			for (int i = 0; i < x; i++) {
				effectsToBeRemovedCauseTheyAreDebuffsSoWeNeedToRemoveThem.get(i).remove(champion);
			}
			Embrace effect = new Embrace(2);
			effect.apply(champion);
			champion.getAppliedEffects().add(effect);
		}
	}

}
