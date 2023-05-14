package model.effects;

import model.world.Champion;

public class Embrace extends Effect {

	public Embrace(int d) {
		super("Embrace", d, EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		c.setCurrentHP(c.getCurrentHP() + (int)(0.2*c.getMaxHP()));
		c.setMana((int)(c.getMana()*1.2));
		c.setAttackDamage((int)(c.getAttackDamage()*1.2));
		c.setSpeed((int)(c.getSpeed()*1.2));
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		c.setAttackDamage((int)(c.getAttackDamage()/1.2));
		c.setSpeed((int)(c.getSpeed()/1.2));
	}
	
}
