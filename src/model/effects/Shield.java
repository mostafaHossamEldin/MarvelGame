package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield(int d) {
		super("Shield", d, EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		c.setSpeed((int) (c.getSpeed() * 1.02));
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		c.setSpeed((int) (c.getSpeed() / 1.02));
	}

}
