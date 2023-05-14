package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	public Silence(int d) {
		super("Silence", d, EffectType.DEBUFF);
	}
	
	public void apply(Champion c) {
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+2);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+2);
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-2);
	}

}
