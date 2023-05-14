package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public class HealingAbility extends Ability {

	private int healAmount;

	public HealingAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int actionsRequired, int healAmount) {
		super(name, manaCost, baseCooldown, castRange, castArea, actionsRequired);
		this.healAmount = healAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}
	
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		for (Damageable damageable : targets) {
			damageable.setCurrentHP(this.healAmount + damageable.getCurrentHP());
		}
	}

}
