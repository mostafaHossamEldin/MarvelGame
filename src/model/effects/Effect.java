package model.effects;

import java.util.ArrayList;

import engine.Game;
import model.world.AntiHero;
import model.world.Champion;

public abstract class Effect implements Cloneable{

	private String name;
	private int duration;
	private EffectType type;

	public Effect(String n, int d, EffectType t) {
		name = n;
		duration = d;
		type = t;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public EffectType getType() {
		return type;
	}
	
	public Object clone() throws CloneNotSupportedException{
		Effect e;
		if (this instanceof Disarm)
			e = new Disarm(this.duration);
		else if (this instanceof Dodge)
			e = new Dodge(this.duration);
		else if (this instanceof Embrace)
			e = new Embrace(this.duration);
		else if (this instanceof PowerUp)
			e = new PowerUp(this.duration);
		else if (this instanceof Root)
			e = new Root(this.duration);
		else if (this instanceof Shield)
			e = new Shield(this.duration);
		else if (this instanceof Shock)
			e = new Shock(this.duration);
		else if (this instanceof Silence)
			e = new Silence(this.duration);
		else if (this instanceof SpeedUp)
			e = new SpeedUp(this.duration);
		else
			e = new Stun(this.duration);
		return e;
	}
	public Champion theChampion() { //gets the wanted champion from avalable champion
		ArrayList<Champion> Champs = Game.getAvailableChampions();
		Champion result = new AntiHero("name", 0, 0, 0, 0, 0, 0);
		for (Champion champion : Champs) {
			if (this.getName().equals(champion.getName())) {
				result = champion;
				break;
			}
		}
		return result;
	}
	
	public boolean contains(Champion c) {
		for (Effect effect : c.getAppliedEffects()) {
			if (this.name.equals(effect.name))
				return true;
		}
		return false;
	}
	
	public abstract void apply(Champion c);

	public abstract void remove(Champion c);

}
