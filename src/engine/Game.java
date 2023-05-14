package engine;

import java.awt.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sql.rowset.serial.SQLOutputImpl;

import exceptions.*;
import model.abilities.*;
import model.effects.*;
import model.world.*;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player p1, Player p2) {
		this.firstLeaderAbilityUsed = false;
		this.secondLeaderAbilityUsed = false;
		this.firstPlayer = p1;
		this.secondPlayer = p2;
		turnOrder = new PriorityQueue(6);
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		placeChampions();
		placeCovers();
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		prepareChampionTurns();
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public static void loadChampions(String filepath) throws IOException {
		availableChampions = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals("H")) {
				Hero h = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			} else if (content[0].equals("V")) {
				Villain h = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			} else if (content[0].equals("A")) {
				AntiHero h = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			}
			line = br.readLine();
		}
		br.close();
	}

	private static Ability getAbilityFromAvailable(String string) {
		for (Ability ability : availableAbilities) {
			if (ability.getName().equals(string))
				return ability;
		}
		return null;
	}

	public static void loadAbilities(String filepath) throws IOException {
		availableAbilities = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals("CC")) {
				availableAbilities.add(new CrowdControlAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), getEffect(content[7], Integer.parseInt(content[8]))));
			} else if (content[0].equals("DMG")) {
				availableAbilities.add(new DamagingAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), Integer.parseInt(content[7])));
			} else if (content[0].equals("HEL")) {
				availableAbilities.add(new HealingAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), Integer.parseInt(content[7])));
			}
			line = br.readLine();
		}
		br.close();
	}

	private static Effect getEffect(String name, int duration) {
		if (name.equals("Dodge"))
			return new Dodge(duration);
		if (name.equals("Disarm"))
			return new Disarm(duration);
		if (name.equals("Embrace"))
			return new Embrace(duration);
		if (name.equals("Stun"))
			return new Stun(duration);
		if (name.equals("Shield"))
			return new Shield(duration);
		if (name.equals("Shock"))
			return new Shock(duration);
		if (name.equals("PowerUp"))
			return new PowerUp(duration);
		if (name.equals("SpeedUp"))
			return new SpeedUp(duration);
		if (name.equals("Silence"))
			return new Silence(duration);
		if (name.equals("Root"))
			return new Root(duration);
		return null;
	}

	public void placeChampions() {
		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
			board[0][i + 1] = firstPlayer.getTeam().get(i);
			firstPlayer.getTeam().get(i).setLocation(new Point(0, i + 1));
			;
		}
		for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
			board[4][i + 1] = secondPlayer.getTeam().get(i);
			secondPlayer.getTeam().get(i).setLocation(new Point(4, i + 1));
			;
		}
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = (int) (Math.random() * 3) + 1;
			int y = (int) (Math.random() * 5);
			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}

	public Player checkGameOver() {
		if (firstPlayer.getTeam().size() == 0)
			return secondPlayer;
		if (secondPlayer.getTeam().size() == 0)
			return firstPlayer;
		return null;
	}

	public boolean moveHelper(Point p) {
		Champion champ = this.getCurrentChampion();
		if ((p.x >= 0 && p.x <= 4 && p.y >= 0 && p.y <= 4)) {// if in border
			if (board[p.x][p.y] == null) {// checks for empty cell
				board[p.x][p.y] = champ;// updates champion.board location
				board[champ.getLocation().x][champ.getLocation().y] = null;// updates champion.board location
				champ.setLocation(p);// updates champion location
				return true;
			}
		}
		return false;
	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		boolean moved = false;
		int champX = getCurrentChampion().getLocation().x;
		int champY = getCurrentChampion().getLocation().y;
		if (getCurrentChampion().getCondition() == Condition.ROOTED) {
			throw new UnallowedMovementException("You are Rooted!");
		}

		if (getCurrentChampion().getCurrentActionPoints() == 0)
			throw new NotEnoughResourcesException("Not enough Action Points!");

		moved = (d == Direction.UP ? moveHelper(new Point(champX - 1, champY))
				: d == Direction.DOWN ? moveHelper(new Point(champX + 1, champY))
						: d == Direction.RIGHT ? moveHelper(new Point(champX, champY + 1))
								: moveHelper(new Point(champX, champY - 1)));
		if (!moved)
			throw new UnallowedMovementException("Invalid Position!");

		getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
	}

	public ArrayList<Damageable> getTarget(Direction d, int range, Player team, boolean isDmgAb) {
		Point p = getCurrentChampion().getLocation();
		ArrayList<Damageable> result = new ArrayList<Damageable>();
		for (int i = 1; i <= range; i++) {
			int xAxis = (d == Direction.UP ? -i : d == Direction.DOWN ? i : 0);
			int yAxis = (d == Direction.RIGHT ? i : d == Direction.LEFT ? -i : 0);
			if (p.x + xAxis >= 0 && p.x + xAxis <= 4 && p.y + yAxis >= 0 && p.y + yAxis <= 4) {
				if (board[p.x + xAxis][p.y + yAxis] instanceof Champion) {
					if (team == helperGetTeam("Ally")
							&& team == helperGetTeam((Champion) board[p.x + xAxis][p.y + yAxis]))
						result.add((Damageable) board[p.x + xAxis][p.y + yAxis]);
					else if (team == helperGetTeam("Enemy")
							&& (helperGetTeam((Champion) board[p.x + xAxis][p.y + yAxis]) == helperGetTeam("Enemy")))
						result.add((Damageable) board[p.x + xAxis][p.y + yAxis]);
				} else if (board[p.x + xAxis][p.y + yAxis] instanceof Cover && isDmgAb) {
					result.add((Damageable) board[p.x + xAxis][p.y + yAxis]);
				}
			}
		}
		return result;
	}

	public ArrayList<Damageable> getTarget(Point p, Player team, boolean isDmgAb) {
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		for (int X = p.x - 1; X <= p.x + 1; X++) {
			for (int Y = p.y - 1; Y <= p.y + 1; Y++) {
				if (!(X < 0 || X > 4 || Y < 0 || Y > 4)) {
					if (board[X][Y] instanceof Champion) {
						if (team == helperGetTeam((Champion) board[X][Y])
								&& (Champion) board[X][Y] != this.getCurrentChampion()) {
							targets.add((Champion) board[X][Y]);
						}
					} else if (board[X][Y] instanceof Cover && isDmgAb) {
						targets.add((Cover) board[X][Y]);
					}
				}
			}
		}
		return targets;
	}

	public ArrayList<Damageable> getTarget(int range, Player team, boolean isDmgAb) {
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		for (int i = 0; i < team.getTeam().size(); i++) {
			if (this.getManhattenDistance(team.getTeam().get(i)) <= range) {
				targets.add(team.getTeam().get(i));
			}
		}
		return targets;
	}

	public Player helperGetTeam(Champion champ) {
		return (firstPlayer.getTeam().contains(champ) ? firstPlayer : secondPlayer);
	}

	public Player helperGetTeam(String s) {
		if (s.equals("Enemy"))
			return (firstPlayer.getTeam().contains(this.getCurrentChampion()) ? secondPlayer : firstPlayer);
		else if (s.equals("Ally"))
			return helperGetTeam(getCurrentChampion());
		return null;
	}

	public static boolean containsEffect(Class e, Champion champ) {
		for (Effect effect : (champ).getAppliedEffects()) {
			if (effect.getClass() == e)
				return true;
		}
		return false;
	}

	public void killOrLive(ArrayList<Damageable> target) {
		for (Damageable damageable : target) {
			Point location = damageable.getLocation();
			if (location != null && damageable.getCurrentHP() == 0) {
				if (damageable instanceof Champion) {
					play("DEATHSOUND.wav");
					((Champion) damageable).setCondition(Condition.KNOCKEDOUT);
					helperGetTeam((Champion) damageable).getTeam().remove(damageable);
					removeTurn((Champion) damageable);
				}
				board[location.x][location.y] = null;
			}
		}
	}
	public void killOrLiveChampions(ArrayList<Champion> target) {
		for (Damageable damageable : target) {
			Point location = damageable.getLocation();
			if (location != null && damageable.getCurrentHP() == 0) {
				if (damageable instanceof Champion) {
					((Champion) damageable).setCondition(Condition.KNOCKEDOUT);
					helperGetTeam((Champion) damageable).getTeam().remove(damageable);
					removeTurn((Champion) damageable);
				}
				board[location.x][location.y] = null;
			}
		}
	}

	public int getManhattenDistance(Damageable target) {
		return Math.abs(getCurrentChampion().getLocation().x - target.getLocation().x)
				+ Math.abs(getCurrentChampion().getLocation().y - target.getLocation().y);
	}

	public void removeTurn(Champion removeChampTurnOrder) {
		PriorityQueue result = new PriorityQueue(6);
		for (int i = 0; i < turnOrder.size(); turnOrder.remove()) {
			if (turnOrder.peekMin() != removeChampTurnOrder)
				result.insert(getCurrentChampion());
		}
		turnOrder = result;
	}

	public void attack(Direction d)
			throws InvalidTargetException, ChampionDisarmedException, NotEnoughResourcesException {
		if (this.getCurrentChampion().getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException("Not enough Action Points!");
		if (containsEffect(Disarm.class, getCurrentChampion()))
			throw new ChampionDisarmedException("You Are Disarmed!");
		ArrayList<Damageable> target = new ArrayList<Damageable>();
		target.addAll(getTarget(d, this.getCurrentChampion().getAttackRange(), helperGetTeam("Enemy"), true));
		if (target.size() == 0)
			throw new InvalidTargetException("No Targets in Range!");
		double extraPer = 1;
		if (target.get(0) instanceof Champion)
			if (!(target.get(0).getClass().getName().equals(getCurrentChampion().getClass().getName())))
				extraPer += 0.5;
		this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints() - 2);
		if (target.get(0) instanceof Champion) {
			for (Effect effect : ((Champion) target.get(0)).getAppliedEffects()) {
				if (effect instanceof Dodge) {
					if (Math.random() < 0.5) {
						return;
					}
				}
				if (effect instanceof Shield) {
					effect.remove((Champion) target.get(0));
					return;
				}
			}
		}
		target.get(0).setCurrentHP(
				target.get(0).getCurrentHP() - (int) (this.getCurrentChampion().getAttackDamage() * extraPer));
		killOrLive(target);
	}

	public void validateCastAbility(Ability a) throws AbilityUseException, NotEnoughResourcesException {
		if (a.getCurrentCooldown() != 0 || containsEffect(Silence.class, getCurrentChampion()))
			throw new AbilityUseException("You cannot apply this Ability!");
		if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()
				|| getCurrentChampion().getMana() < a.getManaCost())
			throw new NotEnoughResourcesException("Insufficient amount of Resources!");
	}

	public Player targetTeam(Ability a) {
		return (a instanceof HealingAbility ? this.helperGetTeam("Ally")
				: a instanceof DamagingAbility ? this.helperGetTeam("Enemy")
						: ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF
								? this.helperGetTeam("Ally")
								: ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF
										? this.helperGetTeam("Enemy")
										: null);
	}

	public void updateResources(Ability a) {
		getCurrentChampion()
				.setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
		getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}

	public void masterExcute(Ability a, ArrayList<Damageable> targets) throws CloneNotSupportedException {
		switch (a.getClass().getName()) {
		case "model.abilities.CrowdControlAbility":
			((CrowdControlAbility) a).execute(targets);
			break;
		case "model.abilities.DamagingAbility":
			((DamagingAbility) a).execute(targets);
			killOrLive(targets);
			break;
		case "model.abilities.HealingAbility":
			((HealingAbility) a).execute(targets);
			break;
		}
	}

	public void castAbility(Ability a) throws AbilityUseException, InvalidTargetException, NotEnoughResourcesException,
			CloneNotSupportedException {

		validateCastAbility(a);
		Player team = targetTeam(a);

		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		switch (a.getCastArea()) {
		case SELFTARGET:
			targets.add(getCurrentChampion());
			break;
		case TEAMTARGET:
			targets.addAll(getTarget(a.getCastRange(), team, (a instanceof DamagingAbility)));
			break;
		case SURROUND:
			targets.addAll(getTarget(getCurrentChampion().getLocation(), team, (a instanceof DamagingAbility)));
			break;
		}
		masterExcute(a, targets);

		updateResources(a);

	}

	public void castAbility(Ability a, Direction d) throws AbilityUseException, NotEnoughResourcesException,
			InvalidTargetException, CloneNotSupportedException {

		validateCastAbility(a);
		Player team = targetTeam(a);

		ArrayList<Damageable> targets = this.getTarget(d, a.getCastRange(), team,
				(a instanceof DamagingAbility ? true : false));

		masterExcute(a, targets);

		updateResources(a);

	}

	public void castAbility(Ability a, int x, int y) throws AbilityUseException, NotEnoughResourcesException,
			InvalidTargetException, CloneNotSupportedException {

		validateCastAbility(a);
		Player team = targetTeam(a);

		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		if ((Damageable) board[x][y] instanceof Champion && team == helperGetTeam((Champion) board[x][y]))
			targets.add((Damageable) board[x][y]);
		if (targets.size() == 0) {
			throw new InvalidTargetException("Invalid Target");
		}
		if (getManhattenDistance(targets.get(0)) > a.getCastRange())
			throw new AbilityUseException("Out of Range!");

		masterExcute(a, targets);

		updateResources(a);

	}

	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException {
		boolean bool = (helperGetTeam(getCurrentChampion()) == firstPlayer ? firstLeaderAbilityUsed
				: secondLeaderAbilityUsed);
		if (bool == true)
			throw new LeaderAbilityAlreadyUsedException("You used this ability already!");
		ArrayList<Champion> targets = new ArrayList<Champion>();
		if (getCurrentChampion() != this.helperGetTeam("Ally").getLeader())
			throw new LeaderNotCurrentException("This is not the Leader!");
		if (getCurrentChampion() instanceof Hero)
			targets.addAll(helperGetTeam("Ally").getTeam());
		else if (getCurrentChampion() instanceof Villain)
			for (Champion champion : helperGetTeam("Enemy").getTeam()) {
				if (champion.getCurrentHP() < champion.getMaxHP() * 0.3)
					targets.add(champion);
			}
		else {
			targets.addAll(firstPlayer.getTeam());
			targets.addAll(secondPlayer.getTeam());
			targets.remove(firstPlayer.getLeader());
			targets.remove(secondPlayer.getLeader());
		}
		getCurrentChampion().useLeaderAbility(targets);
		killOrLiveChampions(targets);
		firstLeaderAbilityUsed = (helperGetTeam(getCurrentChampion()) == firstPlayer ? true : false);
		secondLeaderAbilityUsed = (helperGetTeam(getCurrentChampion()) == firstPlayer ? false : true);
	}

	public void endTurn() {
		turnOrder.remove();
		if (turnOrder.isEmpty())
			prepareChampionTurns();
		int x = 0;
		ArrayList<Effect> expiredEffects = new ArrayList<Effect>();
		for (Effect effect : getCurrentChampion().getAppliedEffects()) {
			effect.setDuration(effect.getDuration() - 1);
			if (effect.getDuration() == 0) {
				expiredEffects.add(effect);
				x++;
			}
		}
		for (Ability ab : this.getCurrentChampion().getAbilities())
			if (ab.getCurrentCooldown() != 0)
				ab.setCurrentCooldown(ab.getCurrentCooldown() - 1);
		if (getCurrentChampion().getCondition() == Condition.INACTIVE) {
			for (int i = 0; i < x; i++)
				expiredEffects.get(i).remove(getCurrentChampion());
			endTurn();
			return;
		}
		for (int i = 0; i < x; i++)
			expiredEffects.get(i).remove(getCurrentChampion());
		getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getMaxActionPointsPerTurn());
	}

	private void prepareChampionTurns() {
		ArrayList<Champion> champs = new ArrayList<Champion>(firstPlayer.getTeam());
		champs.addAll(secondPlayer.getTeam());
		for (Champion champion : champs)
			if (champion.getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(champion);
	}

	public void play(String name) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(name));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}