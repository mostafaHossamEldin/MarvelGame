package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp(int d) {
		super("PowerUp", d, EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		for (Ability ab : c.getAbilities()) {
			if(ab instanceof DamagingAbility)
				((DamagingAbility) ab).setDamageAmount((int) (((DamagingAbility) ab).getDamageAmount() * 1.2));
			else if(ab instanceof HealingAbility)
				((HealingAbility) ab).setHealAmount((int) (((HealingAbility) ab).getHealAmount() * 1.2));
		}
	}
	
	public void remove(Champion c) {
		c.getAppliedEffects().remove(this);
		for (Ability ab : c.getAbilities()) {
			if(ab instanceof DamagingAbility)
				((DamagingAbility) ab).setDamageAmount((int) (((DamagingAbility) ab).getDamageAmount() / 1.2));
			else if(ab instanceof HealingAbility)
				((HealingAbility) ab).setHealAmount((int) (((HealingAbility) ab).getHealAmount() / 1.2));
		}
	}

}
