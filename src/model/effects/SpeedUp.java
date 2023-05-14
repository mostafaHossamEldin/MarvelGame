package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect {

	public SpeedUp(int d) {
		super("SpeedUp", d, EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		c.setSpeed((int) (c.getSpeed()*1.15));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		c.setSpeed((int) (c.getSpeed()/1.15));
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
	}

}
