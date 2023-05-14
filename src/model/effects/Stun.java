package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int d) {
		super("Stun", d, EffectType.DEBUFF);
	}
	
	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		if(super.contains(c)) {
			return;
		}
		for (Effect effect : c.getAppliedEffects()) {
			if (effect instanceof Root)
				c.setCondition(Condition.ROOTED);
				return;
		}
		c.setCondition(Condition.ACTIVE);
	}

}
