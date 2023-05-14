package model.effects;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {

	public Disarm(int d) {
		super("Disarm", d, EffectType.DEBUFF);
	}
	
	public void apply(Champion c) {
		DamagingAbility ab = new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SINGLETARGET, 1, 50);
		c.getAbilities().add(ab);
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		if (super.contains(c))
			return;
		for (Ability ability : c.getAbilities()){
			if(ability.getName().equals("Punch")) {
				c.getAbilities().remove(ability);
				return;
			}
		}
	}

}
