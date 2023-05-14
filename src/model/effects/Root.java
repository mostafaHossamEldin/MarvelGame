package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root(int d) {
		super("Root", d, EffectType.DEBUFF);
	}
	
	public void apply(Champion c) {
		if(c.getCondition() == Condition.ACTIVE) {
			c.setCondition(Condition.ROOTED);
		}
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		//There is something Wrong this logic is not right but in the jUnit it is.
		if (c.getCondition() == Condition.INACTIVE || c.getCondition() == Condition.ROOTED)
			return;
		if(super.contains(c))
			return;
		c.setCondition(Condition.ACTIVE);
	}

}
