package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Condition;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {

	private Effect effect;

	public CrowdControlAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int actionsRequired, Effect effect) {
		super(name, manaCost, baseCooldown, castRange, castArea, actionsRequired);
		this.effect = effect;
	}

	public Effect getEffect() {
		return effect;
	}
	
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		for (Damageable damageable : targets) {
			Effect e = (Effect) this.effect.clone();
			((Champion) damageable).getAppliedEffects().add(e);
			e.apply((Champion) damageable);
		}
	}

}
