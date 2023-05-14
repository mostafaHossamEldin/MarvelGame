package views;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.*;
import model.effects.*;
import model.world.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class View extends JFrame {

	private ActionListener listener;
	private int num = 0;
	private int num2 = 0;
	private Cell[] cells = new Cell[15];
	private Game game;
	private Player player1 = new Player("Player 1");
	private Player player2 = new Player("Player 2");
	private JLabel imagePanel;
	private JPanel imageActualPanel;
	private JLabel nameLabel;
	private JLabel ConditionLabel;
	private JProgressBar hpProgress;
	private JLabel hp;
	private JLabel mana;
	private JLabel actionPoints;
	private JLabel speed;
	private JLabel attackDamage;
	private JLabel attackRange;
	private JLabel championType;
	private ArrayList<JComponent> ability1;
	private ArrayList<JComponent> ability2;
	private ArrayList<JComponent> ability3;
	private JPanel abilitiesPanel;
	private JButton[] grid = new JButton[25];
	private JLabel playerTurnLabel;
	private JPanel abEffectsPanel = new JPanel();
	private boolean castingSingleTarget = false;
	private JButton castAbilityButton;
	private int xxx;
	private int yyy;
	
	private JPanel turnOrderPanel;
	private Ability currentAbilitySelected;
	
	private JButton ability1Button;
	private JButton ability2Button;
	private JButton ability3Button;
	private JButton ability4Button;
	
	private JLabel imagePanel2;
	private JPanel imageActualPanel2;
	private JLabel nameLabel2;
	private JLabel ConditionLabel2;
	private JProgressBar hpProgress2;
	private JLabel hp2;
	private JPanel manaPanel2;
	private JLabel mana2;
	private JLabel actionPoints2;
	private JLabel speed2;
	private JLabel attackDamage2;
	private JLabel attackRange2;
	private JLabel championType2;
	private JPanel abEffectsPanel2 = new JPanel();
	private AbilityButton abilityBox1;
	private AbilityButton abilityBox2;
	private AbilityButton abilityBox3;
	
	private JPanel directionPanel;
	private JLabel abType;
	private JLabel abAOF;
	private JLabel abRange;
	private JLabel abMana;
	private JLabel abAPCost;
	private JLabel abCooldown;
	private JLabel abAmount;
	private JLabel abEffectDuration;
	private JLabel usageLabel;
	private JLabel currentActionLabel;
	private JLabel currentActionLabel2;

	JLabel firstLeaderAbilityLabel;
	JLabel secondLeaderAbilityLabel;
	
	private boolean p1Selected = false;
	private boolean p2Selected = false;
	
	private JPanel panelmap;
	
	ArrayList<Clip> clips = new ArrayList<Clip>();

	public View(ActionListener l) {
		super();// makes the main frame
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setLocationRelativeTo(null);// centers the Frame
		addWindowListener(new terminater());// add a terminator to the
		setTitle("Marvel Ultimate Wars");// you know what it does
		try {
			this.setIconImage((ImageIO.read(getClass().getClassLoader().getResource("Logo.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		listener = l;
		try {
			Game.loadAbilities("Abilities.csv");
			Game.loadChampions("Champions.csv");
		} catch (IOException e) {
		}
		setVisible(true);
	}
	
	public JLabel playerLabel(String s) {
		JLabel label = new JLabel(s, SwingConstants.CENTER);
		label.setFont(new Font("Book Antiqua", Font.PLAIN, 60));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return label;
	}
	
	public void setBounds(JComponent object, int x, int y, int width, int height) {
		Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
		object.setBounds((int)(sc.getWidth() * x/1980), (int)(sc.getHeight() * y/1080), (int)(sc.getWidth() * width/1980), (int)(sc.getHeight() * height/1080));
	}
	
	public static void setBound(JComponent object, double x, double y, double width, double height) { //Use it instead of setBounds. with a range of 0 - 100
		Dimension thisIsTheScreenSizeBoi = Toolkit.getDefaultToolkit().getScreenSize();
		object.setBounds((int)(x*thisIsTheScreenSizeBoi.getWidth()/100), (int)(y*thisIsTheScreenSizeBoi.getHeight()/100), (int)(width*thisIsTheScreenSizeBoi.getWidth()/100), (int)(height*thisIsTheScreenSizeBoi.getHeight()/100));
	}
	
	public void updateChampPanel() {
		nameLabel.setText(game.getCurrentChampion().getName());
		championType.setText((game.getCurrentChampion() instanceof Hero ? "H" : game.getCurrentChampion() instanceof AntiHero ? "AH" : "V"));
		hpProgress.setMaximum(game.getCurrentChampion().getMaxHP());
		hpProgress.setValue(game.getCurrentChampion().getCurrentHP());
		hp.setText(game.getCurrentChampion().getCurrentHP() + "/" + game.getCurrentChampion().getMaxHP());
		mana.setText("" + game.getCurrentChampion().getMana());
		actionPoints.setText("Action Points: " + game.getCurrentChampion().getCurrentActionPoints() + "/" + game.getCurrentChampion().getMaxActionPointsPerTurn());
		speed.setText("Speed: " + game.getCurrentChampion().getSpeed());
		attackDamage.setText("Attack Damage: " + game.getCurrentChampion().getAttackDamage());
		attackRange.setText("Attack Range: " + game.getCurrentChampion().getAttackRange());
		abEffectsPanel.removeAll();
		for(int g = 0; g < (game.getCurrentChampion().getAppliedEffects().size() < 6 ? 6 :game.getCurrentChampion().getAppliedEffects().size()); g++) {
			JPanel lol = new JPanel();
			lol.setBorder(BorderFactory.createRaisedBevelBorder());
			if (g < game.getCurrentChampion().getAppliedEffects().size()) {
				lol.add(new JLabel(game.getCurrentChampion().getAppliedEffects().get(g).getName()));
				lol.add(new JLabel("Duration: " + game.getCurrentChampion().getAppliedEffects().get(g).getDuration()));
				lol.setBackground((game.getCurrentChampion().getAppliedEffects().get(g).getType() == EffectType.DEBUFF ? Color.red : Color.green));
				abEffectsPanel.add(lol);
			}
			else {
				abEffectsPanel.add(lol);
			}
			if (currentAbilitySelected != null && currentAbilitySelected.getCurrentCooldown() == 0)
				castAbilityButton.setEnabled(true);
			else
				castAbilityButton.setEnabled(false);
		}
		if (currentAbilitySelected != null) {
			abCooldown.setText("Cooldown: " + currentAbilitySelected.getCurrentCooldown() + "/" + currentAbilitySelected.getBaseCooldown());
		}
		ability1Button.setText(game.getCurrentChampion().getAbilities().get(0).getName());
		ability2Button.setText(game.getCurrentChampion().getAbilities().get(1).getName());
		ability3Button.setText(game.getCurrentChampion().getAbilities().get(2).getName());
		try {
			imagePanel.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(game.getCurrentChampion().getName() + ".png")).getScaledInstance(12 * this.getWidth()/100,18 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(game.getBoard()[xxx][yyy] == game.getCurrentChampion())
			selectChampOnBoard(xxx, yyy);
		if (game.getCurrentChampion().getAbilities().size() >= 4) {
				ability4Button.setText(game.getCurrentChampion().getAbilities().get(3).getName());
				ability4Button.setVisible(true);
				ability4Button.addActionListener(ActionEvent -> {
					play("anyInGameButton.wav");
					if (!currentActionLabel.getText().equals("Cast") || !currentActionLabel2.getText()
							.equals("\"" + game.getCurrentChampion().getAbilities().get(3).getName() + "\"")) {
						currentAbilitySelected = game.getCurrentChampion().getAbilities().get(3);
						currentActionLabel.setText("Cast");
						currentActionLabel2.setText("\"" + currentAbilitySelected.getName() + "\"");
						abType.setText("Type: " + (currentAbilitySelected instanceof DamagingAbility ? "Dmg Ability"
								: currentAbilitySelected instanceof HealingAbility ? "Healing Ability" : "CC Ability"));
						abAOF.setText("AOE: " + currentAbilitySelected.getCastArea());
						abRange.setText("Cast Range: " + currentAbilitySelected.getCastRange());
						abMana.setText("Mana Cost: " + currentAbilitySelected.getManaCost());
						abAPCost.setText("AP Cost: " + currentAbilitySelected.getRequiredActionPoints());
						abCooldown.setText("Cooldown: " + currentAbilitySelected.getCurrentCooldown() + "/"
								+ currentAbilitySelected.getBaseCooldown());
						abAmount.setText((currentAbilitySelected instanceof DamagingAbility
								? "Dmg Amount: " + ((DamagingAbility) currentAbilitySelected).getDamageAmount()
								: currentAbilitySelected instanceof HealingAbility
										? "Heal Amount: " + ((HealingAbility) currentAbilitySelected).getHealAmount()
										: "Effect: "
												+ ((CrowdControlAbility) currentAbilitySelected).getEffect().getName()));
						abEffectDuration
								.setText((currentAbilitySelected instanceof CrowdControlAbility
										? "Effect Duration: "
												+ ((CrowdControlAbility) currentAbilitySelected).getEffect().getDuration()
										: ""));
						if (currentAbilitySelected.getCastArea() == AreaOfEffect.DIRECTIONAL) {
							castingSingleTarget = false;
							castAbilityButton.setVisible(false);
							directionPanel.setVisible(true);
							usageLabel.setVisible(false);
						} else if (currentAbilitySelected.getCastArea() == AreaOfEffect.SINGLETARGET) {
							castingSingleTarget = true;
							castAbilityButton.setVisible(false);
							directionPanel.setVisible(false);
							usageLabel.setVisible(true);
							usageLabel.setText("Select a Target on Board");
						} else {
							castingSingleTarget = false;
							castAbilityButton.setVisible(true);
							if (currentAbilitySelected.getCurrentCooldown() != 0) {
								castAbilityButton.setEnabled(false);
							} else
								castAbilityButton.setEnabled(true);
							directionPanel.setVisible(false);
							usageLabel.setVisible(false);
						}
					} else {
						currentAbilitySelected = null;
						castAbilityButton.setVisible(false);
						currentActionLabel.setText("");
						currentActionLabel2.setText("");
						castingSingleTarget = false;
						usageLabel.setVisible(false);
						abType.setText("");
						abAOF.setText("");
						abRange.setText("");
						abMana.setText("");
						abAPCost.setText("");
						abCooldown.setText("");
						abAmount.setText("");
						abEffectDuration.setText("");
						directionPanel.setVisible(false);
					}
				});
			}
			else {
				ability4Button.setVisible(false);
			}
		this.revalidate();
		this.repaint();
	}
	
	public void selectChampOnBoard(int a, int b) {
		xxx = a;
		yyy = b;
		nameLabel2.setVisible(true);
		hpProgress2.setVisible(true);
		hp2.setVisible(true);
		mana2.setVisible(true);
		manaPanel2.setVisible(true);
		actionPoints2.setVisible(true);
		speed2.setVisible(true);
		attackDamage2.setVisible(true);
		attackRange2.setVisible(true);
		championType2.setVisible(true);
		imageActualPanel2.setVisible(true);
		abEffectsPanel2.setVisible(false);
		abEffectsPanel2.setVisible(true);
		ConditionLabel2.setVisible(true);
		abilityBox1.setVisible(true);
		abilityBox2.setVisible(true);
		abilityBox3.setVisible(true);
		Champion c = (Champion) game.getBoard()[a][b];
		nameLabel2.setText(c.getName());
		championType2.setText((c instanceof Hero ? "H" : c instanceof AntiHero ? "AH" : "V"));
		hpProgress2.setMaximum(c.getMaxHP());
		hpProgress2.setValue(c.getCurrentHP());
		hp2.setText(c.getCurrentHP() + "/" + c.getMaxHP());
		mana2.setText("" + c.getMana());
		actionPoints2.setText("Action Points: " + c.getCurrentActionPoints() + "/" + c.getMaxActionPointsPerTurn());
		speed2.setText("Speed: " + c.getSpeed());
		attackDamage2.setText("Attack Damage: " + c.getAttackDamage());
		attackRange2.setText("Attack Range: " + c.getAttackRange());
		ConditionLabel2.setText("<html><font color='#C7509F'>" + (game.getCurrentChampion().getCondition() == Condition.INACTIVE ? "Inactive" : game.getCurrentChampion().getCondition() == Condition.ACTIVE ? "Active" : game.getCurrentChampion().getCondition() == Condition.ROOTED ? "Rooted" : "Knocked Out") + "</font></html>");
		abEffectsPanel2.removeAll();
		for(int g = 0; g < (c.getAppliedEffects().size() < 4 ? 4 :c.getAppliedEffects().size()); g++) {
			JPanel lol = new JPanel();
			lol.setBorder(BorderFactory.createRaisedBevelBorder());
			if (g < c.getAppliedEffects().size()) {
				lol.add(new JLabel(c.getAppliedEffects().get(g).getName()));
				lol.add(new JLabel("Duration: " + c.getAppliedEffects().get(g).getDuration()));
				lol.setBackground((c.getAppliedEffects().get(g).getType() == EffectType.DEBUFF ? Color.red : Color.green));
				abEffectsPanel2.add(lol);
			}
			else {
				abEffectsPanel2.add(lol);
			}
		}
		abilityBox1.updatePanel(c.getAbilities().get(0));
		abilityBox2.updatePanel(c.getAbilities().get(1));
		abilityBox3.updatePanel(c.getAbilities().get(2));
		try {
			imagePanel2.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(c.getName() + ".png")).getScaledInstance(12 * this.getWidth()/100,18 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageActualPanel2.setBackground((c == game.helperGetTeam(c).getLeader() ? Color.ORANGE : Color.WHITE));
		this.revalidate();
		this.repaint();
	}
	
	public void updateGamePanel() {
		int lol = 0;
		for(int i = 0; i < Game.getBoardwidth(); i++) {
			for (int j = 0; j < Game.getBoardheight(); j++) {
				grid[lol].removeAll();
				grid[lol].setLayout(null);
				grid[lol].setBorder((new JButton()).getBorder());
				for (ActionListener al : grid[lol].getActionListeners()) {
					grid[lol].removeActionListener(al);
				}
				grid[lol].setActionCommand(i + "" + j);
				if(game.getBoard()[i][j] instanceof Cover) {
					JLabel imgSora = new JLabel();
					setBound(imgSora,0,0,10,14);
					try {
						imgSora.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("Cover.png")).getScaledInstance(10 * this.getWidth()/100,9 * this.getHeight()/100, Image.SCALE_DEFAULT))));
					} catch (IOException e) {
						e.printStackTrace();
					}
					grid[lol].add(imgSora);
					grid[lol].addActionListener(ActionEvent -> {
						nameLabel2.setVisible(false);
						hpProgress2.setVisible(false);
						hp2.setVisible(false);
						manaPanel2.setVisible(false);
						mana2.setVisible(false);
						actionPoints2.setVisible(false);
						speed2.setVisible(false);
						attackDamage2.setVisible(false);
						attackRange2.setVisible(false);
						championType2.setVisible(false);
						imageActualPanel2.setVisible(false);
						abEffectsPanel2.setVisible(false);
						abilityBox1.setVisible(false);
						abilityBox2.setVisible(false);
						abilityBox3.setVisible(false);
						ConditionLabel2.setVisible(false);
					});
					JPanel hpAmount = new JPanel();
					hpAmount.setLayout(null);
					setBound(hpAmount,0,12,10,2);
					hpAmount.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					hpAmount.setBackground(Color.GREEN);
					grid[lol].add(hpAmount);
					JLabel hpp = new JLabel(((Cover) game.getBoard()[i][j]).getCurrentHP()+"", SwingConstants.CENTER);
					hpp.setFont(new Font("", Font.BOLD, 14));
					hpAmount.add(hpp);
					setBound(hpp,0,0,10,2);
				}
				else if (game.getBoard()[i][j] instanceof Champion) {
					JLabel imgSora = new JLabel();
					setBound(imgSora,0,0,10,14);
					try {
						imgSora.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(((Champion)game.getBoard()[i][j]).getName() + ".png")).getScaledInstance(10 * this.getWidth()/100,10 * this.getHeight()/100, Image.SCALE_DEFAULT))));
					} catch (IOException e) {
						e.printStackTrace();
					}
					grid[lol].add(imgSora);
					grid[lol].addActionListener(ActionEvent -> {
						selectChampOnBoard(Integer.parseInt(ActionEvent.getActionCommand().charAt(0)+""),Integer.parseInt(ActionEvent.getActionCommand().charAt(1)+""));
					});
					JLabel nameLabell = new JLabel(((Champion) game.getBoard()[i][j]).getName(), SwingConstants.CENTER);
					nameLabell.setFont(new Font("",Font.BOLD, 14));
					setBound(nameLabell, 0,0,10,2);
					grid[lol].add(nameLabell);
					
					JProgressBar hpAmount = new JProgressBar(0,((Champion) game.getBoard()[i][j]).getMaxHP());
					hpAmount.setValue(((Champion) game.getBoard()[i][j]).getCurrentHP());
					setBound(hpAmount,0,12,10,2);
					hpAmount.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					hpAmount.setForeground(Color.GREEN);
					grid[lol].add(hpAmount);
					JLabel hpp = new JLabel(((Champion) game.getBoard()[i][j]).getCurrentHP() + "/" + ((Champion) game.getBoard()[i][j]).getMaxHP(), SwingConstants.CENTER);
					hpp.setFont(new Font("", Font.BOLD, 14));
					hpAmount.add(hpp);
					setBound(hpp,0,0,10,2);
					
					if(game.helperGetTeam((Champion) game.getBoard()[i][j]) == game.getFirstPlayer()) {
						JPanel pp = new JPanel();
						setBound(pp,0,0,10,14);
						if(game.getCurrentChampion() == (Champion)game.getBoard()[i][j]) {
							grid[lol].setBorder(BorderFactory.createRaisedBevelBorder());
							pp.setBackground(new Color(100,100,185, 255));
						}
						else
							pp.setBackground(new Color(100,100,185, 150));
						grid[lol].add(pp);
					}
					else {
						JPanel pp = new JPanel();
						setBound(pp,0,0,10,14);
						if(game.getCurrentChampion() == (Champion)game.getBoard()[i][j]) {
							grid[lol].setBorder(BorderFactory.createRaisedBevelBorder());
							pp.setBackground(new Color(185,100,100, 255));
						}
						else
							pp.setBackground(new Color(185,100,100, 150));
						grid[lol].add(pp);
					}
				}
				else {
					grid[lol].addActionListener(ActionEvent -> {
						nameLabel2.setVisible(false);
						hpProgress2.setVisible(false);
						hp2.setVisible(false);
						manaPanel2.setVisible(false);
						mana2.setVisible(false);
						actionPoints2.setVisible(false);
						speed2.setVisible(false);
						attackDamage2.setVisible(false);
						attackRange2.setVisible(false);
						championType2.setVisible(false);
						imageActualPanel2.setVisible(false);
						abEffectsPanel2.setVisible(false);
						abilityBox1.setVisible(false);
						abilityBox2.setVisible(false);
						abilityBox3.setVisible(false);
						ConditionLabel2.setVisible(false);
					});
				}
				grid[lol].addActionListener(Action -> {
					if (castingSingleTarget) {
						try {
							game.castAbility(currentAbilitySelected, Integer.parseInt(Action.getActionCommand().charAt(0)+""), Integer.parseInt(Action.getActionCommand().charAt(1)+""));
						} catch (AbilityUseException e) {
							popUp(e.getMessage());
						} catch (NotEnoughResourcesException e) {
							popUp(e.getMessage());
						} catch (InvalidTargetException e) {
							popUp(e.getMessage());
						} catch (CloneNotSupportedException e) {
							popUp(e.getMessage());
						}
						updateChampPanel();
						updateGamePanel();
						updateTurnOrder();
					}
				});
				lol++;
			}
		}
		this.revalidate();
		this.repaint();
	}
	
	public void turnEnded() {
		game.endTurn();
		updateChampPanel();
		updateGamePanel();
		updateTurnOrder();
		playerTurnLabel.setText(game.helperGetTeam(game.getCurrentChampion()).getName() + "'s Turn");
		currentActionLabel.setText("");
		currentActionLabel2.setText("");
		abType.setText("");
		abAOF.setText("");
		abRange.setText("");
		abMana.setText("");
		abAPCost.setText("");
		abCooldown.setText("");
		abAmount.setText("");
		abEffectDuration.setText("");
		directionPanel.setVisible(false);
		castAbilityButton.setVisible(false);
		castingSingleTarget = false;
		currentAbilitySelected = null;
		usageLabel.setText("");
		imageActualPanel.setBackground((game.getCurrentChampion() == game.helperGetTeam(game.getCurrentChampion()).getLeader() ? Color.ORANGE : Color.WHITE));
	}
	
	public void updateTurnOrder(){//Requires More Vision, built Wrong!!!!!!!!!!!!!
		turnOrderPanel.removeAll();
		int lol = 0;
		for(int i = game.getTurnOrder().size(); i > 0; i--,lol++) {
			JPanel panel = new JPanel();
			panel.setBorder(BorderFactory.createRaisedBevelBorder());
			panel.setLayout(null);
			JLabel nameLabell = new JLabel(((Champion) game.getTurnOrder().getElements()[i-1]).getName(), SwingConstants.CENTER);
			nameLabell.setFont(new Font("",Font.BOLD, 12));
			setBound(nameLabell, -2,0,10,2);
			panel.add(nameLabell);
			turnOrderPanel.add(panel);
		}
		for(lol = lol; lol < 6; lol++) {
			JPanel panel = new JPanel();
			panel.setBorder(BorderFactory.createRaisedBevelBorder());
			turnOrderPanel.add(panel);
		}
		this.revalidate();
		this.repaint();
	}
	
	public void takeAction(Direction d, String s, Ability a) {
		if (s.equals("Move")) {
			try {
				game.move(d);
			} catch (UnallowedMovementException e) {
				popUp(e.getMessage());
			} catch (NotEnoughResourcesException e) {
				popUp(e.getMessage());
			}
		}
		else if (s.equals("Attack")) {
			try {
				game.attack(d);
			} catch (InvalidTargetException e) {
				popUp(e.getMessage());
			} catch (ChampionDisarmedException e) {
				popUp(e.getMessage());
			} catch (NotEnoughResourcesException e) {
				popUp(e.getMessage());
			}
		}
		else{
			try {
				game.castAbility(a, d);
			} catch (AbilityUseException e) {
				popUp(e.getMessage());
			} catch (NotEnoughResourcesException e) {
				popUp(e.getMessage());
			} catch (InvalidTargetException e) {
				popUp(e.getMessage());
			} catch (CloneNotSupportedException e) {
				popUp(e.getMessage());
			}
		}
		updateChampPanel();
		updateGamePanel();
		updateTurnOrder();
		if(game.checkGameOver() != null) {
			thatGuyWonLol(game.checkGameOver());
		}
		this.revalidate();
		this.repaint();
	}

	private void thatGuyWonLol(Player p) {
		clearFrame(game);
		this.setLayout(null);
		JLabel virtualFrame = new JLabel();
		clips.get(0).stop();
		clips.remove(clips.get(0));
		try {
			this.revalidate();
			this.repaint();
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		play("Victroy-Theme.wav");
		try {
			virtualFrame.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("BackGround.png")).getScaledInstance(100 * this.getWidth()/100,(int) (100 * this.getHeight()/100), Image.SCALE_DEFAULT))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setBound(virtualFrame,0,0,100,100);
		this.add(virtualFrame);
		JLabel winningLabel = new JLabel("<html><font color='#50C878'>" + p.getName() + " Won!</font></html>", SwingConstants.CENTER);
		winningLabel.setFont(new Font("", Font.BOLD, 120));
		setBound(winningLabel,10,2,80,15);
		virtualFrame.add(winningLabel);
		JLabel borderLabel = new JLabel();
		JLabel wonBoardPhoto = new JLabel();
		setBound(borderLabel, 40, 20, 58, 78);
		setBound(wonBoardPhoto, 1.5, 2.5, 55, 73);
		try {
			borderLabel.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("ImageBorder.png")).getScaledInstance(borderLabel.getWidth(), borderLabel.getHeight(), Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wonBoardPhoto.setIcon(new ImageIcon((Image) new Robot().createScreenCapture(new Rectangle(panelmap.getX(),panelmap.getY(),panelmap.getWidth(), panelmap.getHeight())).getScaledInstance(wonBoardPhoto.getWidth(),wonBoardPhoto.getHeight(), Image.SCALE_DEFAULT)));
		} catch (AWTException e) {
			e.printStackTrace();
		}
		borderLabel.add(wonBoardPhoto);
		virtualFrame.add(borderLabel);
		
		JButton playAgain = new JButton("Play Again");
		playAgain.setFont(new Font("", Font.BOLD, 30));
		playAgain.addActionListener(asfd -> {
			clearFrame(game);
			setPlayers();
		});
		JButton leaveButton = new JButton("Leave Game");
		leaveButton.setFont(new Font("", Font.BOLD, 30));
		leaveButton.addActionListener(asfd -> {
			System.exit(0);
		});
		setBound(playAgain, 10, 63, 20, 15);
		setBound(leaveButton, 10, 80, 20, 15);
		virtualFrame.add(playAgain);
		virtualFrame.add(leaveButton);
		
		this.revalidate();
		this.repaint();
	}

	public void inGameFrame() {
		
//		try {
//			Game.loadAbilities("Abilities.csv");
//		Game.loadChampions("Champions.csv");
//		} catch (IOException e2) {
//			e2.printStackTrace();
//		}
//		
//		for(int i = 0; i < 3; i++) {
//			player1.getTeam().add(Game.getAvailableChampions().get(i));
//			player2.getTeam().add(Game.getAvailableChampions().get(3+i));
//		}
//		player1.setLeader(player1.getTeam().get(0));
//		player2.setLeader(player2.getTeam().get(0));
//		game = new Game(player1, player2);
		
		clips.get(0).close();
		clips.remove(clips.get(0));
		play("MAINMENU THEME.wav");
		
		JPanel panelright = new JPanel();
		panelright.setPreferredSize(new Dimension(31 * this.getWidth()/100, 65 * this.getHeight()/100));
		this.add(panelright, BorderLayout.EAST);
		panelright.setLayout(null);
		panelright.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JLayeredPane panelLeft = new JLayeredPane();
		panelLeft.setPreferredSize(new Dimension(16 * this.getWidth()/100, 65 * this.getHeight()/100));
		this.add(panelLeft, BorderLayout.WEST);
		panelLeft.setLayout(null);
		panelLeft.setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel panelDown = new JPanel();
		panelDown.setPreferredSize(new Dimension(16 * this.getWidth()/100, 10 * this.getHeight()/100));
		this.add(panelDown, BorderLayout.SOUTH);
		panelDown.setLayout(null);
		panelDown.setBorder(BorderFactory.createLineBorder(Color.black));
		
		panelmap = new JPanel();
		panelmap.setLayout(new GridLayout(5, 5));
		panelmap.setBackground(new Color(76, 114, 148));
		panelmap.setBorder(BorderFactory.createEmptyBorder(2 * this.getWidth()/100, 3 * this.getHeight()/100, 2 * this.getWidth()/100, 3 * this.getHeight()/100));
		this.getContentPane().add(panelmap, BorderLayout.CENTER);

		for (int i = 0; i < 25; i++) {
			grid[i] = new JButton();
			grid[i].setLayout(new GridLayout(1,1));
			grid[i].setOpaque(false);
			grid[i].setContentAreaFilled(false);
			grid[i].setBorderPainted(true);// we will turn it to false b3deen
			try {
				Image img = ImageIO.read(getClass().getClassLoader().getResource("GREENBLOCK.png")); //Scale the image
				grid[i].setIcon((new ImageIcon(img)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			panelmap.add(grid[i]);
		}
		updateGamePanel();
		int x = 17;
		
		JPanel namepanel = new JPanel();
		namepanel.setLayout(null);
		namepanel.setPreferredSize(new Dimension(36 * this.getWidth()/100, 12 * this.getHeight()/100));
		namepanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(namepanel, BorderLayout.NORTH);
		
		JLabel Player1Label = playerLabel(player1.getName());
		JLabel Player2Label = playerLabel(player2.getName());
		setBound(Player1Label,1, 1, 31, 10);
		setBound(Player2Label,68, 1, 31, 10);
		namepanel.add(Player1Label);
		namepanel.add(Player2Label);
		
		playerTurnLabel = new JLabel(game.helperGetTeam(game.getCurrentChampion()).getName() + "'s Turn", SwingConstants.CENTER);
		setBound(playerTurnLabel, 32,1,36,10);
		namepanel.add(playerTurnLabel);
		playerTurnLabel.setFont(new Font("", Font.BOLD, 50));

		imageActualPanel = new JPanel();
		imageActualPanel.setLayout(null);
		imageActualPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		imageActualPanel.setBackground((game.helperGetTeam(game.getCurrentChampion()).getLeader() == game.getCurrentChampion() ? Color.ORANGE : Color.WHITE));
		setBound(imageActualPanel, x, 6, 12, 18);
		panelright.add(imageActualPanel);

		imagePanel = new JLabel();
		setBound(imagePanel,0,0,12,18);
		try {
			imagePanel.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(game.getCurrentChampion().getName() + ".png")).getScaledInstance(12 * this.getWidth()/100,18 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageActualPanel.add(imagePanel);

		championType = new JLabel((game.getCurrentChampion() instanceof Hero ? "H" : game.getCurrentChampion() instanceof AntiHero ? "AH" : "V"), SwingConstants.CENTER);
		championType.setFont(new Font("", Font.BOLD, 24));
		championType.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(championType,0,0,2.5,3.5);
		imageActualPanel.add(championType);
		
		ConditionLabel = new JLabel("<html><font color='#C7509F'>" + (game.getCurrentChampion().getCondition() == Condition.INACTIVE ? "Inactive" : game.getCurrentChampion().getCondition() == Condition.ACTIVE ? "Active" : game.getCurrentChampion().getCondition() == Condition.ROOTED ? "Rooted" : "Knocked Out") + "</font></html>", SwingConstants.RIGHT);
		ConditionLabel.setFont(new Font("",Font.BOLD,18));
		setBound(ConditionLabel,3.5,0,8,3.5);
		imagePanel.add(ConditionLabel);
		
		nameLabel = new JLabel(game.getCurrentChampion().getName(), SwingConstants.CENTER);
		setBound(nameLabel,x,2,12,4);
		nameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameLabel.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		panelright.add(nameLabel);
		
		hpProgress = new JProgressBar(0,game.getCurrentChampion().getMaxHP());
		hpProgress.setValue(game.getCurrentChampion().getCurrentHP());
		setBound(hpProgress,x,24,12,4);
		hpProgress.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hpProgress.setForeground(Color.GREEN);
		panelright.add(hpProgress);
		hp = new JLabel(game.getCurrentChampion().getCurrentHP() + "/" + game.getCurrentChampion().getMaxHP(), SwingConstants.CENTER);
		hp.setFont(new Font("", Font.BOLD, 18));
		hpProgress.add(hp);
		setBound(hp,0,0,12,4);

		JPanel manaPanel = new JPanel();
		panelright.add(manaPanel);
		mana = new JLabel("" + game.getCurrentChampion().getMana(), SwingConstants.CENTER);
		mana.setFont(new Font("", Font.BOLD, 18));
		manaPanel.add(mana);
		setBound(manaPanel,x,28,12,4);
		manaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manaPanel.setBackground(Color.CYAN);

		actionPoints = organizedLabel("Action Points: " + game.getCurrentChampion().getCurrentActionPoints() + "/" + game.getCurrentChampion().getMaxActionPointsPerTurn(), x, 33);
		panelright.add(actionPoints);

		speed = organizedLabel("Speed: " + game.getCurrentChampion().getSpeed(), x, 38);
		panelright.add(speed);

		attackDamage = organizedLabel("Attack Damage: " + game.getCurrentChampion().getAttackDamage(), x, 43);
		panelright.add(attackDamage);

		attackRange = organizedLabel("Attack Range: " + game.getCurrentChampion().getAttackRange(), x, 48);
		panelright.add(attackRange);
		
		int x2 = 2;
		
		JLabel detailsLabel1 = new JLabel("Select a Champion", SwingConstants.CENTER);
		detailsLabel1.setFont(new Font("", Font.BOLD, 18));
		setBound(detailsLabel1,x2,1,12,3.5);
		JLabel detailsLabel2 = new JLabel("to View Details", SwingConstants.CENTER);
		detailsLabel2.setFont(new Font("", Font.BOLD, 18));
		setBound(detailsLabel2,x2,4,12,2);
		panelLeft.add(detailsLabel1);
		panelLeft.add(detailsLabel2);
		
		imageActualPanel2 = new JPanel();
		imageActualPanel2.setLayout(null);
		imageActualPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		imageActualPanel2.setBackground((game.helperGetTeam(game.getCurrentChampion()).getLeader() == game.getCurrentChampion() ? Color.ORANGE : Color.WHITE));
		setBound(imageActualPanel2, x2, 11, 12, 18);
		panelLeft.add(imageActualPanel2);

		imagePanel2 = new JLabel();
		setBound(imagePanel2,0,0,12,18);
		try {
			imagePanel2.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(game.getCurrentChampion().getName() + ".png")).getScaledInstance(12 * this.getWidth()/100,18 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageActualPanel2.add(imagePanel2);
		
		championType2 = new JLabel((game.getCurrentChampion() instanceof Hero ? "H" : game.getCurrentChampion() instanceof AntiHero ? "AH" : "V"), SwingConstants.CENTER);
		championType2.setFont(new Font("", Font.BOLD, 24));
		championType2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(championType2,0,0,2.5,3.5);
		imageActualPanel2.add(championType2);

		ConditionLabel2 = new JLabel("<html><font color='#C7509F'>" + (game.getCurrentChampion().getCondition() == Condition.INACTIVE ? "Inactive" : game.getCurrentChampion().getCondition() == Condition.ACTIVE ? "Active" : game.getCurrentChampion().getCondition() == Condition.ROOTED ? "Rooted" : "Knocked Out") + "</font></html>", SwingConstants.RIGHT);
		ConditionLabel2.setFont(new Font("",Font.BOLD,18));
		setBound(ConditionLabel2,3.5,0,8,3.5);
		imagePanel2.add(ConditionLabel2);
		
		nameLabel2 = new JLabel(game.getCurrentChampion().getName(), SwingConstants.CENTER);
		setBound(nameLabel2,x2,7,12,4);
		nameLabel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameLabel2.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		panelLeft.add(nameLabel2);
		
		hpProgress2 = new JProgressBar(0,game.getCurrentChampion().getMaxHP());
		hpProgress2.setValue(game.getCurrentChampion().getCurrentHP());
		setBound(hpProgress2,x2,29,12,4);
		hpProgress2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hpProgress2.setForeground(Color.GREEN);
		panelLeft.add(hpProgress2);
		hp2 = new JLabel(game.getCurrentChampion().getCurrentHP() + "/" + game.getCurrentChampion().getMaxHP(), SwingConstants.CENTER);
		hp2.setFont(new Font("", Font.BOLD, 18));
		hpProgress2.add(hp2);
		setBound(hp2,0,0,12,4);

		manaPanel2 = new JPanel();
		panelLeft.add(manaPanel2);
		mana2 = new JLabel("" + game.getCurrentChampion().getMana(), SwingConstants.CENTER);
		mana2.setFont(new Font("", Font.BOLD, 18));
		manaPanel2.add(mana2);
		setBound(manaPanel2,x2,33,12,4);
		manaPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manaPanel2.setBackground(Color.CYAN);

		actionPoints2 = organizedLabel("Action Points: " + game.getCurrentChampion().getCurrentActionPoints() + "/" + game.getCurrentChampion().getMaxActionPointsPerTurn(), x2, 38);
		panelLeft.add(actionPoints2);

		speed2 = organizedLabel("Speed: " + game.getCurrentChampion().getSpeed(), x2, 43);
		panelLeft.add(speed2);

		attackDamage2 = organizedLabel("Attack Damage: " + game.getCurrentChampion().getAttackDamage(), x2, 48);
		panelLeft.add(attackDamage2);

		attackRange2 = organizedLabel("Attack Range: " + game.getCurrentChampion().getAttackRange(), x2, 53);
		panelLeft.add(attackRange2);
		
		JButton endTurnButton = new JButton("End Turn");
		endTurnButton.setFont(new Font("", Font.BOLD, 20));
		setBound(endTurnButton, 87, 1, 12, 8);
		endTurnButton.addActionListener(ActionEvent ->{
			play("anyInGameButton.wav");
			this.turnEnded();
		});
		panelDown.add(endTurnButton);
		
		JButton ULAButton = new JButton("Use Leader Ability");
		ULAButton.setFont(new Font("", Font.BOLD, 20));
		setBound(ULAButton, 72, 1, 14, 8);
		ULAButton.addActionListener(ActionEvent ->{
			play("anyInGameButton.wav");
			try {
				game.useLeaderAbility();
			} catch (LeaderNotCurrentException e) {
				popUp(e.getMessage());
			} catch (LeaderAbilityAlreadyUsedException e) {
				popUp(e.getMessage());
			}
			firstLeaderAbilityLabel.setText("Player 1 Leader Ability: " + (game.isFirstLeaderAbilityUsed() ? "Used" : "Not Used"));
			secondLeaderAbilityLabel.setText("Player 2 Leader Ability: " + (game.isSecondLeaderAbilityUsed() ? "Used" : "Not Used"));
			updateChampPanel();
			updateGamePanel();
			updateTurnOrder();
		});
		panelDown.add(ULAButton);
		
		firstLeaderAbilityLabel = new JLabel("Player 1 Leader Ability: " + (game.isFirstLeaderAbilityUsed() ? "Used" : "Not Used"));
		secondLeaderAbilityLabel = new JLabel("Player 2 Leader Ability: " + (game.isSecondLeaderAbilityUsed() ? "Used" : "Not Used"));
		firstLeaderAbilityLabel.setFont(new Font("", Font.BOLD, 10));
		secondLeaderAbilityLabel.setFont(new Font("", Font.BOLD, 10));
		setBound(firstLeaderAbilityLabel, 62, 1, 10, 4);
		setBound(secondLeaderAbilityLabel, 62, 5, 10, 4);
		panelDown.add(firstLeaderAbilityLabel);
		panelDown.add(secondLeaderAbilityLabel);

		JLabel actionInfo = new JLabel();
		try {
			actionInfo.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("Card.png")).getScaledInstance(14 * this.getWidth()/100,36 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setBound(actionInfo, 2, 40, 14, 36);
		actionInfo.setLayout(null);
		panelright.add(actionInfo);
		
		JLabel actionInfoLabel = new JLabel("Action Info", SwingConstants.CENTER);
		actionInfoLabel.setFont(new Font("",Font.BOLD,20));
		setBound(actionInfoLabel,0,0,14,4);
		actionInfo.add(actionInfoLabel);

		currentActionLabel = new JLabel("", SwingConstants.CENTER);
		currentActionLabel.setFont(new Font("", Font.BOLD, 20));
		setBound(currentActionLabel,0,4,14,4);
		actionInfo.add(currentActionLabel);

		currentActionLabel2 = new JLabel("", SwingConstants.CENTER);
		currentActionLabel2.setFont(new Font("", Font.BOLD, 20));
		setBound(currentActionLabel2,0,7,14,4);
		actionInfo.add(currentActionLabel2);
		
		directionPanel = new JPanel();
		actionInfo.add(directionPanel);
		directionPanel.setLayout(null);
		directionPanel.setBackground(new Color(0,0,0,0));
		setBound(directionPanel, 2, 22.85, 10, 10);
		directionPanel.setVisible(false);
		
		JButton up = new JButton();
		up.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			takeAction(Direction.UP, currentActionLabel.getText(), currentAbilitySelected);
		});
		setBound(up, 3, 0, 4, 5);
		JButton down = new JButton();
		down.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			takeAction(Direction.DOWN, currentActionLabel.getText(), currentAbilitySelected);
		});
		setBound(down, 3, 5, 4, 5);
		JButton left = new JButton();
		left.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			takeAction(Direction.LEFT, currentActionLabel.getText(), currentAbilitySelected);
		});
		setBound(left, 0, 5, 3, 5);
		JButton right = new JButton();
		right.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			takeAction(Direction.RIGHT, currentActionLabel.getText(), currentAbilitySelected);
		});
		setBound(right, 7, 5, 3, 5);
		up.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		down.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		left.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		directionPanel.add(up);
		directionPanel.add(down);
		directionPanel.add(left);
		directionPanel.add(right);
		try {
			up.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("UpArrow.png")).getScaledInstance(4 * this.getWidth()/100,5 * this.getHeight()/100, Image.SCALE_DEFAULT))));
			down.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("DownArrow.png")).getScaledInstance(4 * this.getWidth()/100,5 * this.getHeight()/100, Image.SCALE_DEFAULT))));
			left.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("LeftArrow.png")).getScaledInstance(3 * this.getWidth()/100,6 * this.getHeight()/100, Image.SCALE_DEFAULT))));
			right.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("RightArrow.png")).getScaledInstance(3 * this.getWidth()/100,6 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		abType = new JLabel("", SwingConstants.LEFT);
		setBound(abType, 0.5, 13, 8, 2);
		abAOF = new JLabel("", SwingConstants.CENTER);
		setBound(abAOF, 0, 9.5, 14, 4);
		abRange = new JLabel("", SwingConstants.LEFT);
		setBound(abRange, 8, 15, 8, 2);
		abMana = new JLabel("", SwingConstants.LEFT);
		setBound(abMana, 8, 17, 8, 2);
		abAPCost = new JLabel("", SwingConstants.LEFT);
		setBound(abAPCost, 8, 19, 8, 2);
		abCooldown = new JLabel("", SwingConstants.LEFT);
		setBound(abCooldown, 8, 13, 8, 2);
		abAmount = new JLabel("", SwingConstants.LEFT);
		setBound(abAmount, 0.5, 15, 8, 2);
		abEffectDuration = new JLabel("", SwingConstants.LEFT);
		setBound(abEffectDuration, 0.5, 17, 8, 2);
		usageLabel = new JLabel("", SwingConstants.CENTER);
		usageLabel.setFont(new Font("", Font.BOLD, 18));
		setBound(usageLabel, 0.5, 25.85, 13, 4);
		castAbilityButton = new JButton("Cast Ability");
		setBound(castAbilityButton, 2, 26, 10, 6);
		castAbilityButton.setVisible(false);
		castAbilityButton.addActionListener(CastAction -> {
			play("anyInGameButton.wav");
			try {
				game.castAbility(currentAbilitySelected);
				updateChampPanel();
				updateGamePanel();
				updateTurnOrder();
			} catch (AbilityUseException e) {
				popUp(e.getMessage());
			} catch (InvalidTargetException e) {
				popUp(e.getMessage());
			} catch (NotEnoughResourcesException e) {
				popUp(e.getMessage());
			} catch (CloneNotSupportedException e) {
				popUp(e.getMessage());
			}
		});
		
		actionInfo.add(abType);
		actionInfo.add(abAOF);
		actionInfo.add(abRange);
		actionInfo.add(abMana);
		actionInfo.add(abAPCost);
		actionInfo.add(abCooldown);
		actionInfo.add(abAmount);
		actionInfo.add(abEffectDuration);
		actionInfo.add(usageLabel);
		actionInfo.add(castAbilityButton);
		
		JButton moveButton = new JButton("");
		try {
			moveButton.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("Move Button.png")).getScaledInstance(14 * this.getWidth()/100,7 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBound(moveButton, x-15, 2, 14, 7);
		moveButton.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			currentAbilitySelected = null;
			if(!currentActionLabel.getText().equals("Move")) {
				directionPanel.setVisible(true);
				castAbilityButton.setVisible(false);
				usageLabel.setText("");
				currentActionLabel.setText("Move");
				currentActionLabel2.setText("");
				abType.setText("");
				abAOF.setText("");
				abRange.setText("");
				abMana.setText("");
				abAPCost.setText("");
				abCooldown.setText("");
				abAmount.setText("");
				abEffectDuration.setText("");
				castAbilityButton.setVisible(false);
			}
			else {
				currentActionLabel.setText("");
				currentActionLabel2.setText("");
				directionPanel.setVisible(false);
			}
		});
		panelright.add(moveButton);
		
		JButton attackButton = new JButton();
		try {
			attackButton.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("Attack Button.png")).getScaledInstance(14 * this.getWidth()/100,7 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBound(attackButton, x-15, 9, 14, 7);
		attackButton.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			currentAbilitySelected = null;
			if(!currentActionLabel.getText().equals("Attack")) {
				directionPanel.setVisible(true);
				usageLabel.setText("");
				currentActionLabel.setText("Attack");
				currentActionLabel2.setText("");
				abType.setText("");
				abAOF.setText("");
				abRange.setText("");
				abMana.setText("");
				abAPCost.setText("");
				abCooldown.setText("");
				abAmount.setText("");
				abEffectDuration.setText("");
				castAbilityButton.setVisible(false);
			}
			else {
				currentActionLabel.setText("");
				currentActionLabel2.setText("");
				directionPanel.setVisible(false);
			}
		});
		panelright.add(attackButton);

		ability1Button = new JButton(game.getCurrentChampion().getAbilities().get(0).getName());
		ability1Button.setFont(new Font("", Font.BOLD, 18));
		setBound(ability1Button, x-15, 17, 14, 5);
		ability1Button.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			if(!currentActionLabel.getText().equals("Cast") || !currentActionLabel2.getText().equals("\"" + game.getCurrentChampion().getAbilities().get(0).getName() + "\"")) {
				currentAbilitySelected = game.getCurrentChampion().getAbilities().get(0);
				currentActionLabel.setText("Cast");
				currentActionLabel2.setText("\"" + currentAbilitySelected.getName() + "\"");
				abType.setText("Type: " + (currentAbilitySelected instanceof DamagingAbility ? "Dmg Ability" : currentAbilitySelected instanceof HealingAbility ? "Healing Ability" : "CC Ability"));
				abAOF.setText("AOE: " + currentAbilitySelected.getCastArea());
				abRange.setText("Cast Range: " + currentAbilitySelected.getCastRange());
				abMana.setText("Mana Cost: " + currentAbilitySelected.getManaCost());
				abAPCost.setText("AP Cost: " + currentAbilitySelected.getRequiredActionPoints());
				abCooldown.setText("Cooldown: " + currentAbilitySelected.getCurrentCooldown() + "/" + currentAbilitySelected.getBaseCooldown());
				abAmount.setText((currentAbilitySelected instanceof DamagingAbility ? "Dmg Amount: " + ((DamagingAbility) currentAbilitySelected).getDamageAmount() : currentAbilitySelected instanceof HealingAbility ? "Heal Amount: " + ((HealingAbility) currentAbilitySelected).getHealAmount() : "Effect: " + ((CrowdControlAbility)currentAbilitySelected).getEffect().getName()));
				abEffectDuration.setText((currentAbilitySelected instanceof CrowdControlAbility ? "Effect Duration: " + ((CrowdControlAbility)currentAbilitySelected).getEffect().getDuration() : ""));
				if(currentAbilitySelected.getCastArea() == AreaOfEffect.DIRECTIONAL) {
					castingSingleTarget = false;
					castAbilityButton.setVisible(false);
					directionPanel.setVisible(true);
					usageLabel.setVisible(false);
				}
				else if(currentAbilitySelected.getCastArea() == AreaOfEffect.SINGLETARGET) {
					castingSingleTarget = true;
					castAbilityButton.setVisible(false);
					directionPanel.setVisible(false);
					usageLabel.setVisible(true);
					usageLabel.setText("Select a Target on Board");
				}
				else {
					castingSingleTarget = false;
					castAbilityButton.setVisible(true);
					if (currentAbilitySelected.getCurrentCooldown() != 0) {
						castAbilityButton.setEnabled(false);
					}
					else
						castAbilityButton.setEnabled(true);
					directionPanel.setVisible(false);
					usageLabel.setVisible(false);
				}
			}
			else {
				currentAbilitySelected = null;
				castAbilityButton.setVisible(false);
				currentActionLabel.setText("");
				currentActionLabel2.setText("");
				castingSingleTarget = false;
				usageLabel.setVisible(false);
				abType.setText("");
				abAOF.setText("");
				abRange.setText("");
				abMana.setText("");
				abAPCost.setText("");
				abCooldown.setText("");
				abAmount.setText("");
				abEffectDuration.setText("");
				directionPanel.setVisible(false);
			}
		});
		panelright.add(ability1Button);

		ability2Button = new JButton(game.getCurrentChampion().getAbilities().get(1).getName());
		ability2Button.setFont(new Font("", Font.BOLD, 18));
		setBound(ability2Button, x-15, 22, 14, 5);
		ability2Button.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			if(!currentActionLabel.getText().equals("Cast") || !currentActionLabel2.getText().equals("\"" + game.getCurrentChampion().getAbilities().get(1).getName() + "\"")) {
				currentAbilitySelected = game.getCurrentChampion().getAbilities().get(1);
				currentActionLabel.setText("Cast");
				currentActionLabel2.setText("\"" + game.getCurrentChampion().getAbilities().get(1).getName() + "\"");
				abType.setText("Type: " + (currentAbilitySelected instanceof DamagingAbility ? "Dmg Ability" : currentAbilitySelected instanceof HealingAbility ? "Healing Ability" : "CC Ability"));
				abAOF.setText("AOE: " + currentAbilitySelected.getCastArea());
				abRange.setText("Cast Range: " + currentAbilitySelected.getCastRange());
				abMana.setText("Mana Cost: " + currentAbilitySelected.getManaCost());
				abAPCost.setText("AP Cost: " + currentAbilitySelected.getRequiredActionPoints());
				abCooldown.setText("Cooldown: " + currentAbilitySelected.getCurrentCooldown() + "/" + currentAbilitySelected.getBaseCooldown());
				abAmount.setText((currentAbilitySelected instanceof DamagingAbility ? "Dmg Amount: " + ((DamagingAbility) currentAbilitySelected).getDamageAmount() : currentAbilitySelected instanceof HealingAbility ? "Heal Amount: " + ((HealingAbility) currentAbilitySelected).getHealAmount() : "Effect: " + ((CrowdControlAbility)currentAbilitySelected).getEffect().getName()));
				abEffectDuration.setText((currentAbilitySelected instanceof CrowdControlAbility ? "Effect Duration: " + ((CrowdControlAbility)currentAbilitySelected).getEffect().getDuration() : ""));
				if(currentAbilitySelected.getCastArea() == AreaOfEffect.DIRECTIONAL) {
					castingSingleTarget = false;
					castAbilityButton.setVisible(false);
					directionPanel.setVisible(true);
					usageLabel.setVisible(false);
				}
				else if(currentAbilitySelected.getCastArea() == AreaOfEffect.SINGLETARGET) {
					castingSingleTarget = true;
					castAbilityButton.setVisible(false);
					directionPanel.setVisible(false);
					usageLabel.setVisible(true);
					usageLabel.setText("Select a Target on Board");
				}
				else {
					castingSingleTarget = false;
					castAbilityButton.setVisible(true);
					if (currentAbilitySelected.getCurrentCooldown() != 0) {
						castAbilityButton.setEnabled(false);
					}
					else
						castAbilityButton.setEnabled(true);
					directionPanel.setVisible(false);
					usageLabel.setVisible(false);
				}
			}
			else {
				currentAbilitySelected = null;
				castAbilityButton.setVisible(false);
				currentActionLabel.setText("");
				currentActionLabel2.setText("");
				castingSingleTarget = false;
				usageLabel.setVisible(false);
				abType.setText("");
				abAOF.setText("");
				abRange.setText("");
				abMana.setText("");
				abAPCost.setText("");
				abCooldown.setText("");
				abAmount.setText("");
				abEffectDuration.setText("");
				directionPanel.setVisible(false);
			}
		});
		panelright.add(ability2Button);

		ability3Button = new JButton(game.getCurrentChampion().getAbilities().get(2).getName());
		ability3Button.setFont(new Font("", Font.BOLD, 18));
		setBound(ability3Button, x-15, 27, 14, 5);
		ability3Button.addActionListener(ActionEvent -> {
			play("anyInGameButton.wav");
			if(!currentActionLabel.getText().equals("Cast") || !currentActionLabel2.getText().equals("\"" + game.getCurrentChampion().getAbilities().get(2).getName() + "\"")) {
				currentAbilitySelected = game.getCurrentChampion().getAbilities().get(2);
				currentActionLabel.setText("Cast");
				currentActionLabel2.setText("\"" + currentAbilitySelected.getName() + "\"");
				abType.setText("Type: " + (currentAbilitySelected instanceof DamagingAbility ? "Dmg Ability" : currentAbilitySelected instanceof HealingAbility ? "Healing Ability" : "CC Ability"));
				abAOF.setText("AOE: " + currentAbilitySelected.getCastArea());
				abRange.setText("Cast Range: " + currentAbilitySelected.getCastRange());
				abMana.setText("Mana Cost: " + currentAbilitySelected.getManaCost());
				abAPCost.setText("AP Cost: " + currentAbilitySelected.getRequiredActionPoints());
				abCooldown.setText("Cooldown: " + currentAbilitySelected.getCurrentCooldown() + "/" + currentAbilitySelected.getBaseCooldown());
				abAmount.setText((currentAbilitySelected instanceof DamagingAbility ? "Dmg Amount: " + ((DamagingAbility) currentAbilitySelected).getDamageAmount() : currentAbilitySelected instanceof HealingAbility ? "Heal Amount: " + ((HealingAbility) currentAbilitySelected).getHealAmount() : "Effect: " + ((CrowdControlAbility)currentAbilitySelected).getEffect().getName()));
				abEffectDuration.setText((currentAbilitySelected instanceof CrowdControlAbility ? "Effect Duration: " + ((CrowdControlAbility)currentAbilitySelected).getEffect().getDuration() : ""));
				if(currentAbilitySelected.getCastArea() == AreaOfEffect.DIRECTIONAL) {
					castingSingleTarget = false;
					castAbilityButton.setVisible(false);
					directionPanel.setVisible(true);
					usageLabel.setVisible(false);
				}
				else if(currentAbilitySelected.getCastArea() == AreaOfEffect.SINGLETARGET) {
					castingSingleTarget = true;
					castAbilityButton.setVisible(false);
					directionPanel.setVisible(false);
					usageLabel.setVisible(true);
					usageLabel.setText("Select a Target on Board");
				}
				else {
					castingSingleTarget = false;
					castAbilityButton.setVisible(true);
					if (currentAbilitySelected.getCurrentCooldown() != 0) {
						castAbilityButton.setEnabled(false);
					}
					else
						castAbilityButton.setEnabled(true);
					directionPanel.setVisible(false);
					usageLabel.setVisible(false);
				}
			}
			else {
				currentAbilitySelected = null;
				castAbilityButton.setVisible(false);
				currentActionLabel.setText("");
				currentActionLabel2.setText("");
				castingSingleTarget = false;
				usageLabel.setVisible(false);
				abType.setText("");
				abAOF.setText("");
				abRange.setText("");
				abMana.setText("");
				abAPCost.setText("");
				abCooldown.setText("");
				abAmount.setText("");
				abEffectDuration.setText("");
				directionPanel.setVisible(false);
			}
		});
		panelright.add(ability3Button);
		
		ability4Button = new JButton();
		ability4Button.setFont(new Font("", Font.BOLD, 18));
		setBound(ability4Button, x - 15, 32, 14, 5);
		panelright.add(ability4Button);
		if (game.getCurrentChampion().getAbilities().size() >= 4) {
			ability4Button.setText(game.getCurrentChampion().getAbilities().get(3).getName());
			ability4Button.setVisible(true);
			ability4Button.addActionListener(ActionEvent -> {
				play("anyInGameButton.wav");
				if (!currentActionLabel.getText().equals("Cast") || !currentActionLabel2.getText()
						.equals("\"" + game.getCurrentChampion().getAbilities().get(2).getName() + "\"")) {
					currentAbilitySelected = game.getCurrentChampion().getAbilities().get(2);
					currentActionLabel.setText("Cast");
					currentActionLabel2.setText("\"" + currentAbilitySelected.getName() + "\"");
					abType.setText("Type: " + (currentAbilitySelected instanceof DamagingAbility ? "Dmg Ability"
							: currentAbilitySelected instanceof HealingAbility ? "Healing Ability" : "CC Ability"));
					abAOF.setText("AOE: " + currentAbilitySelected.getCastArea());
					abRange.setText("Cast Range: " + currentAbilitySelected.getCastRange());
					abMana.setText("Mana Cost: " + currentAbilitySelected.getManaCost());
					abAPCost.setText("AP Cost: " + currentAbilitySelected.getRequiredActionPoints());
					abCooldown.setText("Cooldown: " + currentAbilitySelected.getCurrentCooldown() + "/"
							+ currentAbilitySelected.getBaseCooldown());
					abAmount.setText((currentAbilitySelected instanceof DamagingAbility
							? "Dmg Amount: " + ((DamagingAbility) currentAbilitySelected).getDamageAmount()
							: currentAbilitySelected instanceof HealingAbility
									? "Heal Amount: " + ((HealingAbility) currentAbilitySelected).getHealAmount()
									: "Effect: "
											+ ((CrowdControlAbility) currentAbilitySelected).getEffect().getName()));
					abEffectDuration
							.setText((currentAbilitySelected instanceof CrowdControlAbility
									? "Effect Duration: "
											+ ((CrowdControlAbility) currentAbilitySelected).getEffect().getDuration()
									: ""));
					if (currentAbilitySelected.getCastArea() == AreaOfEffect.DIRECTIONAL) {
						castingSingleTarget = false;
						castAbilityButton.setVisible(false);
						directionPanel.setVisible(true);
						usageLabel.setVisible(false);
					} else if (currentAbilitySelected.getCastArea() == AreaOfEffect.SINGLETARGET) {
						castingSingleTarget = true;
						castAbilityButton.setVisible(false);
						directionPanel.setVisible(false);
						usageLabel.setVisible(true);
						usageLabel.setText("Select a Target on Board");
					} else {
						castingSingleTarget = false;
						castAbilityButton.setVisible(true);
						if (currentAbilitySelected.getCurrentCooldown() != 0) {
							castAbilityButton.setEnabled(false);
						} else
							castAbilityButton.setEnabled(true);
						directionPanel.setVisible(false);
						usageLabel.setVisible(false);
					}
				} else {
					currentAbilitySelected = null;
					castAbilityButton.setVisible(false);
					currentActionLabel.setText("");
					currentActionLabel2.setText("");
					castingSingleTarget = false;
					usageLabel.setVisible(false);
					abType.setText("");
					abAOF.setText("");
					abRange.setText("");
					abMana.setText("");
					abAPCost.setText("");
					abCooldown.setText("");
					abAmount.setText("");
					abEffectDuration.setText("");
					directionPanel.setVisible(false);
				}
			});
		}
		else {
			ability4Button.setVisible(false);
		}
		
		abEffectsPanel = new JPanel();
		setBound(abEffectsPanel, x, 54, 12, 22);
		GridLayout layout = new GridLayout(3,3);
		layout.setVgap((int) (0.5 * this.getHeight()/100));
		layout.setHgap((int) (0.5 * this.getHeight()/100));
		abEffectsPanel.setLayout(layout);
		
		abEffectsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		panelright.add(abEffectsPanel);
		
		setBound(abEffectsPanel2, x2, 58, 12, 12);
		GridLayout layout2 = new GridLayout(2,3);
		layout2.setVgap((int) (0.5 * this.getHeight()/100));
		layout2.setHgap((int) (0.5 * this.getHeight()/100));
		abEffectsPanel2.setLayout(layout2);

		abEffectsPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
		panelLeft.add(abEffectsPanel2);
		
		abilityBox1 = new AbilityButton("Ab 1",x2-1,71,4,6);
		panelLeft.add(abilityBox1, JLayeredPane.DRAG_LAYER);
		abilityBox2 = new AbilityButton("Ab 2",x2+4,71,4,6);
		panelLeft.add(abilityBox2, JLayeredPane.DRAG_LAYER);
		abilityBox3 = new AbilityButton("Ab 3",x2+9,71,4,6);
		panelLeft.add(abilityBox3, JLayeredPane.DRAG_LAYER);

		for(int g = 0; g < (game.getCurrentChampion().getAppliedEffects().size() < 6 ? 6 :game.getCurrentChampion().getAppliedEffects().size()); g++) {
			JPanel lol = new JPanel();
			lol.setBorder(BorderFactory.createRaisedBevelBorder());
			if (g < game.getCurrentChampion().getAppliedEffects().size()) {
				lol.add(new JLabel(game.getCurrentChampion().getAppliedEffects().get(g).getName()));
				lol.add(new JLabel("Duration: " + game.getCurrentChampion().getAppliedEffects().get(g).getDuration()));
				lol.setBackground((game.getCurrentChampion().getAppliedEffects().get(g).getType() == EffectType.DEBUFF ? Color.red : Color.green));
				abEffectsPanel.add(lol);
			}
			else {
				abEffectsPanel.add(lol);
			}
		}
		
		turnOrderPanel = new JPanel();
		setBound(turnOrderPanel, 25, 1, 36 ,8);
		turnOrderPanel.setLayout(new GridLayout(1,6));
		updateTurnOrder();
		turnOrderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelDown.add(turnOrderPanel);

		nameLabel2.setVisible(false);
		hpProgress2.setVisible(false);
		hp2.setVisible(false);
		manaPanel2.setVisible(false);
		mana2.setVisible(false);
		actionPoints2.setVisible(false);
		speed2.setVisible(false);
		attackDamage2.setVisible(false);
		attackRange2.setVisible(false);
		championType2.setVisible(false);
		imageActualPanel2.setVisible(false);
		abEffectsPanel2.setVisible(false);
		abilityBox1.setVisible(false);
		abilityBox2.setVisible(false);
		abilityBox3.setVisible(false);
		ConditionLabel2.setVisible(false);
		
		this.revalidate();
		this.repaint();

	}
	
	public JLabel organizedLabel(String s, int x, int y) {
		JLabel label = new JLabel(s, SwingConstants.CENTER);
		label.setFont(new Font("", Font.BOLD, 18));
		setBound(label,x,y,12,4);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return label;
	}
		
	public ArrayList<JComponent> organizedAbility(ArrayList<JComponent> arr, Ability s, double x, double y) {
		JLabel abNumber = new JLabel((arr == ability1 ? "1st Ability" : arr == ability2 ? "2nd Ability" : "3rd Ability"), SwingConstants.CENTER);
		arr = new ArrayList<JComponent>();
		abNumber.setFont(new Font("", Font.BOLD, 10));
		setBound(abNumber,x,y,9.66,3);
		abNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel name = new JLabel("Apility Name: " + s.getName(), SwingConstants.CENTER);
		name.setFont(new Font("", Font.BOLD, 10));
		setBound(name,x,y+3,9.66,3);
		name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		String t = (s instanceof DamagingAbility ? "Damaging Ability" : s instanceof HealingAbility ? "Healing Ability" : "Crowd Control Ability");
		JLabel type = new JLabel("Type: " + t, SwingConstants.CENTER);
		type.setFont(new Font("", Font.BOLD, 10));
		setBound(type,x,y+6,9.66,3);
		type.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel AOF = new JLabel("Cast Area: " + s.getCastArea(), SwingConstants.CENTER);
		AOF.setFont(new Font("", Font.BOLD, 10));
		setBound(AOF,x,y+9,9.66,3);
		AOF.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel castRange = new JLabel("Cast Range: " + s.getCastRange(), SwingConstants.CENTER);
		castRange.setFont(new Font("", Font.BOLD, 10));
		setBound(castRange,x,y+12,9.66,3);
		castRange.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel AP = new JLabel("AP Cost: " + s.getRequiredActionPoints(), SwingConstants.CENTER);
		AP.setFont(new Font("", Font.BOLD, 10));
		setBound(AP,x,y+15,9.66,3);
		AP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel coolDown = new JLabel("Cooldown: " + s.getBaseCooldown(), SwingConstants.CENTER);
		coolDown.setFont(new Font("", Font.BOLD, 10));
		setBound(coolDown,x,y+18,9.66,3);
		coolDown.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel abTypeAmount = new JLabel(
				(t.equals("Damaging Ability") ? "Damaging Amount: " + ((DamagingAbility) s).getDamageAmount()
						: t.equals("Healing Ability") ? "Healing Amount: " + ((HealingAbility) s).getHealAmount() : "Effect: " + ((CrowdControlAbility) s).getEffect().getName()),
				SwingConstants.CENTER);
		abTypeAmount.setFont(new Font("", Font.BOLD, 10));
		setBound(abTypeAmount,x,y+21,9.66,3);
		abTypeAmount.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		arr.add(abNumber);
		arr.add(name);
		arr.add(type);
		arr.add(AOF);
		arr.add(castRange);
		arr.add(AP);
		arr.add(coolDown);
		arr.add(abTypeAmount);
		return arr;
	}
	
	public void choiceOfLeader(int nn, String s) {
		if(s.equals("p1")) {
			num = nn;
			for(int i = 0; i<player1.getTeam().size(); i++) {
				cells[i].setBackground(Color.GRAY);
			}
			nameLabel.setText(cells[num].getChamp().getName());
			championType.setText((cells[num].getChamp() instanceof Hero ? "H" : cells[num].getChamp() instanceof AntiHero ? "AH" : "V"));
			hpProgress.setMaximum(cells[num].getChamp().getMaxHP());
			hpProgress.setValue(cells[num].getChamp().getCurrentHP());
			hp.setText(cells[num].getChamp().getCurrentHP() + "/" + cells[num].getChamp().getMaxHP());
			mana.setText("" + cells[num].getChamp().getMana());
			actionPoints.setText("Action Points: " + cells[num].getChamp().getCurrentActionPoints() + "/" + cells[num].getChamp().getMaxActionPointsPerTurn());
			speed.setText("Speed: " + cells[num].getChamp().getSpeed());
			attackDamage.setText("Attack Damage: " + cells[num].getChamp().getAttackDamage());
			attackRange.setText("Attack Range: " + cells[num].getChamp().getAttackRange());
			cells[num].setBackground(Color.YELLOW);
			imagePanel.setIcon((new ImageIcon(cells[num].getImg().getScaledInstance(16 * this.getWidth()/100,15 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		}
		else if(s.equals("p2")){
			num2 = player1.getTeam().size() + nn;
			for(int i = 0; i < player2.getTeam().size(); i++) {
				cells[player1.getTeam().size()+i].setBackground(Color.GRAY);
			}
			nameLabel2.setText(cells[num2].getChamp().getName());
			championType2.setText((cells[num2].getChamp() instanceof Hero ? "H" : cells[num2].getChamp() instanceof AntiHero ? "AH" : "V"));
			hpProgress2.setMaximum(cells[num2].getChamp().getMaxHP());
			hpProgress2.setValue(cells[num2].getChamp().getCurrentHP());
			hp2.setText(cells[num2].getChamp().getCurrentHP() + "/" + cells[num2].getChamp().getMaxHP());
			mana2.setText("" + cells[num2].getChamp().getMana());
			actionPoints2.setText("Action Points: " + cells[num2].getChamp().getCurrentActionPoints() + "/" + cells[num2].getChamp().getMaxActionPointsPerTurn());
			speed2.setText("Speed: " + cells[num2].getChamp().getSpeed());
			attackDamage2.setText("Attack Damage: " + cells[num2].getChamp().getAttackDamage());
			attackRange2.setText("Attack Range: " + cells[num2].getChamp().getAttackRange());
			cells[num2].setBackground(Color.YELLOW);
			imagePanel2.setIcon((new ImageIcon(cells[num2].getImg().getScaledInstance(16 * this.getWidth()/100,15 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		}
	}
	
	public void chooseLeader() {
		
		num2 = player1.getTeam().size();
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(20 * this.getWidth()/100,12 * this.getHeight()/100));// sets size
		topPanel.setBackground(new Color(76, 114, 148));
		topPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, this.getWidth()/100 * 2, 0, this.getWidth()/100 * 2));
		topPanel.setOpaque(true);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		
		JLabel p1 = new JLabel(player1.getName(), SwingConstants.CENTER);
		p1.setFont(new Font("", Font.BOLD, 60));
		topPanel.add(p1, BorderLayout.WEST);
		
		JLabel label = new JLabel("Choose your Leaders!", SwingConstants.CENTER);
		label.setFont(new Font("", Font.BOLD, 60));
		topPanel.add(label, BorderLayout.CENTER);

		JLabel p2 = new JLabel(player2.getName(), SwingConstants.CENTER);
		p2.setFont(new Font("", Font.BOLD, 60));
		topPanel.add(p2, BorderLayout.EAST);
		
		JPanel p1Panel = new JPanel();
		p1Panel.setLayout(null);
		p1Panel.setPreferredSize(new Dimension(this.getWidth()/2,0));// sets size
		p1Panel.setBackground(new Color(76, 114, 148));
		p1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		p1Panel.setOpaque(true);
		this.getContentPane().add(p1Panel, BorderLayout.WEST);

		JPanel p2Panel = new JPanel();
		p2Panel.setLayout(null);
		p2Panel.setPreferredSize(new Dimension(this.getWidth()/2,0));// sets size
		p2Panel.setBackground(new Color(76, 114, 148));
		p2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		p2Panel.setOpaque(true);
		this.getContentPane().add(p2Panel, BorderLayout.EAST);
		
		imagePanel = new JLabel();
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(imagePanel,17,30,16,24);
		imagePanel.setIcon((new ImageIcon(cells[num].getImg().getScaledInstance(16 * this.getWidth()/100,15 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		p1Panel.add(imagePanel);

		championType = new JLabel((cells[num].getChamp() instanceof Hero ? "H" : cells[num].getChamp() instanceof AntiHero ? "AH" : "V"), SwingConstants.CENTER);
		championType.setFont(new Font("", Font.BOLD, 30));
		championType.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(championType,17,30,3,4.5);
		p1Panel.add(championType);

		JPanel namePanel = new JPanel();
		p1Panel.add(namePanel);//name of everychamp
		setBound(namePanel,17,26,16,4);
		namePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameLabel = new JLabel(cells[num].getChamp().getName(), SwingConstants.CENTER);
		nameLabel.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		namePanel.add(nameLabel);
		
		hpProgress = new JProgressBar(0,cells[num].getChamp().getMaxHP());
		hpProgress.setValue(cells[num].getChamp().getCurrentHP());
		setBound(hpProgress,17,54,16,4);
		hpProgress.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hpProgress.setForeground(Color.GREEN);
		p1Panel.add(hpProgress);
		hp = new JLabel(cells[num].getChamp().getCurrentHP() + "/" + cells[num].getChamp().getMaxHP(), SwingConstants.CENTER);
		hp.setFont(new Font("", Font.BOLD, 18));
		hpProgress.add(hp);
		setBound(hp,0,0,16,4);

		JPanel manaPanel = new JPanel();
		p1Panel.add(manaPanel);
		mana = new JLabel("" + cells[num].getChamp().getMana(), SwingConstants.CENTER);
		mana.setFont(new Font("", Font.BOLD, 18));
		manaPanel.add(mana);
		setBound(manaPanel,17,58,16,4);
		manaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manaPanel.setBackground(Color.CYAN);

		actionPoints = organizedLabel("Action Points: " + cells[num].getChamp().getCurrentActionPoints() + "/" + cells[num].getChamp().getMaxActionPointsPerTurn(), 25, 64);
		p1Panel.add(actionPoints);

		speed = organizedLabel("Speed: " + cells[num].getChamp().getSpeed(), 13, 64);
		p1Panel.add(speed);

		attackDamage = organizedLabel("Attack Damage: " + cells[num].getChamp().getAttackDamage(), 25, 69);
		p1Panel.add(attackDamage);

		attackRange = organizedLabel("Attack Range: " + cells[num].getChamp().getAttackRange(), 13, 69);
		p1Panel.add(attackRange);
		
		
		imagePanel2 = new JLabel();
		imagePanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(imagePanel2,17,30,16,24);
		imagePanel2.setIcon((new ImageIcon(cells[num2].getImg().getScaledInstance(16 * this.getWidth()/100,15 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		p2Panel.add(imagePanel2);

		championType2 = new JLabel((cells[num2].getChamp() instanceof Hero ? "H" : cells[num2].getChamp() instanceof AntiHero ? "AH" : "V"), SwingConstants.CENTER);
		championType2.setFont(new Font("", Font.BOLD, 30));
		championType2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(championType2,17,30,3,4.5);
		p2Panel.add(championType2);

		JPanel namePanel2 = new JPanel();
		p2Panel.add(namePanel2);
		setBound(namePanel2,17,26,16,4);
		namePanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameLabel2 = new JLabel(cells[num2].getChamp().getName(), SwingConstants.CENTER);
		nameLabel2.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		namePanel2.add(nameLabel2);
		
		hpProgress2 = new JProgressBar(0,cells[num2].getChamp().getMaxHP());
		hpProgress2.setValue(cells[num2].getChamp().getCurrentHP());
		setBound(hpProgress2,17,54,16,4);
		hpProgress2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hpProgress2.setForeground(Color.GREEN);
		p2Panel.add(hpProgress2);
		hp2 = new JLabel(cells[num2].getChamp().getCurrentHP() + "/" + cells[num2].getChamp().getMaxHP(), SwingConstants.CENTER);
		hp2.setFont(new Font("", Font.BOLD, 18));
		hpProgress2.add(hp2);
		setBound(hp2,0,0,16,4);

		JPanel manaPanel2 = new JPanel();
		p2Panel.add(manaPanel2);
		mana2 = new JLabel("" + cells[num2].getChamp().getMana(), SwingConstants.CENTER);
		mana2.setFont(new Font("", Font.BOLD, 18));
		manaPanel2.add(mana2);
		setBound(manaPanel2,17,58,16,4);
		manaPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manaPanel2.setBackground(Color.CYAN);

		actionPoints2 = organizedLabel("Action Points: " + cells[num2].getChamp().getCurrentActionPoints() + "/" + cells[num2].getChamp().getMaxActionPointsPerTurn(), 25, 64);
		p2Panel.add(actionPoints2);

		speed2 = organizedLabel("Speed: " + cells[num2].getChamp().getSpeed(), 13, 64);
		p2Panel.add(speed2);

		attackDamage2 = organizedLabel("Attack Damage: " + cells[num2].getChamp().getAttackDamage(), 25, 69);
		p2Panel.add(attackDamage2);

		attackRange2 = organizedLabel("Attack Range: " + cells[num2].getChamp().getAttackRange(), 13, 69);
		p2Panel.add(attackRange2);
		
		p1Selected = false;
		p2Selected = false;
		
		JButton button = new JButton("Select Leader");
		button.setFont(new Font("", Font.BOLD, 40));
		setBound(button, 13,75,24,12);
		p1Panel.add(button);
		button.addActionListener(ActionEvent -> {
			play("selectbuttonchamp.wav");
			for(int i = 0; i < player1.getTeam().size(); i++) {
				cells[i].setEnabled(false);
			}
			player1.setLeader(cells[num].getChamp());
			p1Selected = true;
			if(p2Selected) {
				clearFrame(game);
				inGameFrame();
			}
		});

		JButton button2 = new JButton("Select Leader");
		button2.setFont(new Font("", Font.BOLD, 40));
		setBound(button2, 13,75,24,12);
		p2Panel.add(button2);
		button2.addActionListener(ActionEvent -> {
			play("selectbuttonchamp.wav");
			for(int i = player1.getTeam().size(); i < player1.getTeam().size()+player2.getTeam().size(); i++) {
				cells[i].setEnabled(false);
			}
			player2.setLeader(cells[num2].getChamp());
			p2Selected = true;
			if(p1Selected) {
				clearFrame(game);
				inGameFrame();
			}
		});
		
		num = 0;
		cells = new Cell[player1.getTeam().size()*2];
		for (int i = 0; i < player1.getTeam().size(); i++) {
			Cell p1Cell = new Cell(player1.getTeam().get(i), i, this,10,15);
			Cell p2Cell = new Cell(player2.getTeam().get(i), i, this,10,15);
			p1Cell.setActionCommand("p1"+i);
			p2Cell.setActionCommand("p2"+i);
			setBound(p1Cell,5 + (i*15),10,10,15);
			setBound(p2Cell,5 + (i*15),10,10,15);
			p1Panel.add(p1Cell);
			p2Panel.add(p2Cell);
			p1Cell.setIcon((new ImageIcon(p1Cell.getImg().getScaledInstance(10 * this.getWidth()/100,10 * this.getHeight()/100, Image.SCALE_DEFAULT))));
			p1Cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			p2Cell.setIcon((new ImageIcon(p2Cell.getImg().getScaledInstance(10 * this.getWidth()/100,10 * this.getHeight()/100, Image.SCALE_DEFAULT))));
			p2Cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cells[i] = p1Cell;
			cells[i+player1.getTeam().size()] = p2Cell;
		}
		choiceOfLeader(0, "p1");
		choiceOfLeader(0, "p2");
		this.revalidate();
		this.repaint();
		
	}

	public void chooseChampion() {

		num = 0;
		num2 = 0;
		try {
			Game.loadAbilities("Abilities.csv");
			Game.loadChampions("Champions.csv");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		cells = new Cell[Game.getAvailableChampions().size()];
		JLabel label = new JLabel(player1.getName() + ", Choose your Champions!", SwingConstants.CENTER);
		label.setFont(new Font("", Font.BOLD, 70));
		label.setPreferredSize(new Dimension(20 * this.getWidth()/100,12 * this.getHeight()/100));// sets size
		label.setBackground(new Color(76, 114, 148));
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setOpaque(true);
		this.getContentPane().add(label, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel();
		GridLayout layout = new GridLayout(4, 5);
		centerPanel.setLayout(layout);
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		centerPanel.setBackground(new Color(76, 114, 148));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(this.getHeight()/100 * 2, this.getWidth()/100 * 2, this.getHeight()/100 * 2, this.getWidth()/100 * 2));
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		int i = 0;
		for (Champion champion : Game.getAvailableChampions()) {
			cells[i] = new Cell(champion, i, this, 17, 20);
			cells[i].setIcon((new ImageIcon(cells[i].getImg().getScaledInstance(12 * this.getWidth()/100,15 * this.getHeight()/100, Image.SCALE_DEFAULT))));
			cells[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			centerPanel.add(cells[i]);
			i++;
		}
		
		JPanel champPanel = new JPanel();// makes a panel to divide frame
		champPanel.setLayout(null);
		
		imagePanel = new JLabel();
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(imagePanel,7,6,16,24);
		imagePanel.setIcon((new ImageIcon(cells[num].getImg().getScaledInstance(16 * this.getWidth()/100,21 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		champPanel.add(imagePanel);

		championType = new JLabel((cells[num].getChamp() instanceof Hero ? "H" : cells[num].getChamp() instanceof AntiHero ? "AH" : "V"), SwingConstants.CENTER);
		championType.setFont(new Font("", Font.BOLD, 30));
		championType.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBound(championType,7,6,3,4.5);
		champPanel.add(championType);

		JPanel namePanel = new JPanel();
		champPanel.add(namePanel);//name of everychamp
		setBound(namePanel,7,2,16,4);
		namePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameLabel = new JLabel(cells[num].getChamp().getName(), SwingConstants.CENTER);
		nameLabel.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		namePanel.add(nameLabel);
		
		hpProgress = new JProgressBar(0,cells[num].getChamp().getMaxHP());
		hpProgress.setValue(cells[num].getChamp().getCurrentHP());
		setBound(hpProgress,7,30,16,4);
		hpProgress.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hpProgress.setForeground(Color.GREEN);
		champPanel.add(hpProgress);
		hp = new JLabel(cells[num].getChamp().getCurrentHP() + "/" + cells[num].getChamp().getMaxHP(), SwingConstants.CENTER);
		hp.setFont(new Font("", Font.BOLD, 18));
		hpProgress.add(hp);
		setBound(hp,0,0,16,4);

		JPanel manaPanel = new JPanel();
		champPanel.add(manaPanel);
		mana = new JLabel("" + cells[num].getChamp().getMana(), SwingConstants.CENTER);
		mana.setFont(new Font("", Font.BOLD, 18));
		manaPanel.add(mana);
		setBound(manaPanel,7,34,16,4);
		manaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manaPanel.setBackground(Color.CYAN);

		actionPoints = organizedLabel("Action Points: " + cells[num].getChamp().getCurrentActionPoints() + "/" + cells[num].getChamp().getMaxActionPointsPerTurn(), 15, 40);
		champPanel.add(actionPoints);

		speed = organizedLabel("Speed: " + cells[num].getChamp().getSpeed(), 3, 40);
		champPanel.add(speed);

		attackDamage = organizedLabel("Attack Damage: " + cells[num].getChamp().getAttackDamage(), 15, 45);
		champPanel.add(attackDamage);

		attackRange = organizedLabel("Attack Range: " + cells[num].getChamp().getAttackRange(), 3, 45);
		champPanel.add(attackRange);
		abilitiesPanel = new JPanel();
		abilitiesPanel.setLayout(null);
		setBound(abilitiesPanel, 0, 52, 29, 24);
		champPanel.add(abilitiesPanel);
		ability1 = organizedAbility(ability1, cells[num].getChamp().getAbilities().get(0), 0, 0);
		ability2 = organizedAbility(ability2, cells[num].getChamp().getAbilities().get(1), 9.66, 0);
		ability3 = organizedAbility(ability3, cells[num].getChamp().getAbilities().get(2), 19.33, 0);
		for (int j = 0; j < ability1.size(); j++) {
			abilitiesPanel.add(ability1.get(j));
			abilitiesPanel.add(ability2.get(j));
			abilitiesPanel.add(ability3.get(j));
		}
		
		JButton button = new JButton("Select Champ");
		button.setFont(new Font("", Font.BOLD, 32));
		setBound(button, 5, 78, 19, 8);
		champPanel.add(button);
		button.addActionListener(ActionEvent -> {
			try {
				choose();
				if (player1.getTeam().size() == 3) {
					label.setText(player2.getName() + ", Choose your Champions!");
				}
			} catch (Exception e) {
				popUp(e.getMessage());
			}
		});
		
		cells[num].setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		champPanel.setPreferredSize(new Dimension(29 * this.getWidth()/100,0 * this.getHeight()/100));// sets size
		champPanel.setBackground(new Color(150, 150, 150));
		champPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		this.getContentPane().add(champPanel, BorderLayout.EAST);
		this.revalidate();
		this.repaint();

	}

	public void popUp(String message) {
		play("throwexception.wav");
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void choose() throws Exception {
		if (!cells[num].isEnabled()) {
			throw new Exception("This Champion is Already Choosen!");
		}
		play("selectbuttonchamp.wav");
		if(player1.getTeam().size() < 3) {
			cells[num].setEnabled(false);// by5le al button does nothing
			cells[num].setBackground(Color.BLUE);
			player1.getTeam().add(cells[num].getChamp());
		}
		else if(player2.getTeam().size() < 3) {
			cells[num].setEnabled(false);// by5le al button does nothing
			cells[num].setBackground(Color.RED);
			player2.getTeam().add(cells[num].getChamp());
		}
		if(player2.getTeam().size() == 3) {
			game = new Game(player1, player2);
			this.clearFrame(game);
			this.chooseLeader();
		}
	}
	
	public void selectChamp(int n) {
		for (Cell cell : cells) { //cells for champs cells
			cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		num = n;
		cells[num].setBorder(BorderFactory.createLineBorder(Color.WHITE));
		nameLabel.setText(cells[num].getChamp().getName());
		championType.setText((cells[num].getChamp() instanceof Hero ? "H" : cells[num].getChamp() instanceof AntiHero ? "AH" : "V"));
		hpProgress.setMaximum(cells[num].getChamp().getMaxHP());
		hpProgress.setValue(cells[num].getChamp().getCurrentHP());
		hp.setText(cells[num].getChamp().getCurrentHP() + "/" + cells[num].getChamp().getMaxHP());
		mana.setText("" + cells[num].getChamp().getMana());
		actionPoints.setText("Action Points: " + cells[num].getChamp().getCurrentActionPoints() + "/" + cells[num].getChamp().getMaxActionPointsPerTurn());
		speed.setText("Speed: " + cells[num].getChamp().getSpeed());
		attackDamage.setText("Attack Damage: " + cells[num].getChamp().getAttackDamage());
		attackRange.setText("Attack Range: " + cells[num].getChamp().getAttackRange());
		abilitiesPanel.removeAll();
		ability1 = organizedAbility(ability1, cells[num].getChamp().getAbilities().get(0), 0, 0);
		ability2 = organizedAbility(ability2, cells[num].getChamp().getAbilities().get(1), 9.66, 0);
		ability3 = organizedAbility(ability3, cells[num].getChamp().getAbilities().get(2), 19.33, 0);
		for (int j = 0; j < ability1.size(); j++) {
			abilitiesPanel.add(ability1.get(j));
			abilitiesPanel.add(ability2.get(j));
			abilitiesPanel.add(ability3.get(j));
		}
		imagePanel.setIcon((new ImageIcon(cells[num].getImg().getScaledInstance(16 * this.getWidth()/100,21 * this.getHeight()/100, Image.SCALE_DEFAULT))));
		this.revalidate();
		this.repaint();
	}

	public void play(String name) {
		for (int i = 0; i < clips.size(); i++) {
			if (!clips.get(i).isActive()) {
				clips.remove(clips.get(i));
				i--;
			}
		}
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(name));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			if(name.equals("in gametheme.wav") || name.equals("Victroy-Theme.wav") || name.equals("MAINMENU THEME.wav")) {
				clip.loop(1000);
			}
			clips.add(clip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean selected = false;
	public void setPlayers() {
		selected = false;
		for (int i = 0; i < clips.size(); i++) {
			clips.get(i).close();
			clips.remove(clips.get(i));
			i--;
		}
		
		this.setLayout(null);
		play("MAINMENU THEME.wav");
		JLabel image = new JLabel();
		setBound(image,0,0,100,100);
		try {
			image.setIcon((new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("BackGroundNames.png")).getScaledInstance(100 * this.getWidth()/100,(int) (100 * this.getHeight()/100), Image.SCALE_DEFAULT))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.add(image);
		
		JTextArea text1 = new JTextArea("Enter your Name");
		text1.setFont(new Font("", Font.BOLD, 35));
		JTextArea text2 = new JTextArea("Enter your Name");
		text2.setFont(new Font("", Font.BOLD, 35));
		text1.setBorder(null);
		text2.setBorder(null);
		text1.setBackground(new Color(0,0,0,0));
		text2.setBackground(new Color(0,0,0,0));
		setBound(text1,7,27,25,8);
		setBound(text2,68,27,25,8);
		image.add(text1);
		image.add(text2);
		
		JButton button = new JButton("");
		button.setFont(new Font("", Font.BOLD, 65));
		setBound(button, 39.25, 84, 20.5, 13.2);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setOpaque(false);
		image.add(button);
		button.addActionListener(ActionEvent -> {
			if (text1.getText().equals(text2.getText())) {
				popUp("Please Choose Different Names!");
			}
			else {
				play("STARTGAMEBUTTON.wav");
				selected = true;
				player1 = new Player(text1.getText());
				player2 = new Player(text2.getText());
				this.clearFrame(game);
				this.setLayout(new BorderLayout());
				this.chooseChampion();
			}
		});
		while(!selected) {
			this.revalidate();
			this.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	

	public void clearFrame(Game g) {
		this.game = g;
		this.getContentPane().removeAll();
		this.revalidate();
		this.repaint();
	}
	
}
