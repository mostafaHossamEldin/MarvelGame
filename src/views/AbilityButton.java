package views;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import model.abilities.*;

public class AbilityButton extends JPanel implements ActionListener{
	
	private JButton lolol;
	private JPanel mainPanel;
	private JLabel nameLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel typeLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel AOFLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel rangeLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel manaLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel actionLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel cooldownLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel amountLabel = new JLabel("", SwingConstants.LEFT);
	private JLabel effectDuration = new JLabel("", SwingConstants.LEFT);
	
	private int width;
	
	public AbilityButton(String s, int x, int y,int width, int height) {
		this.width = width;
		this.setLayout(null);
		lolol = new JButton(s);
		this.add(lolol);
		lolol.addActionListener(this);
		if (s.equals("Ab 1")) {
			View.setBound(this, x-1, y-27, width+6, height+27);
			View.setBound(lolol, 1, 27, width, height);
		}
		else if(s.equals("Ab 2")) {
			View.setBound(this, x-3, y-27, width+6, height+27);
			View.setBound(lolol, 3, 27, width, height);
		}
		else {
			View.setBound(this, x-5, y-27, width+6, height+27);
			View.setBound(lolol, 5, 27, width, height);
		}
		
		this.setBackground(new Color(155,155,155,0));
		this.setVisible(true);
		
		
		mainPanel = new JPanel();
		View.setBound(mainPanel, 0, 0, width+6, 27);
		mainPanel.setBackground(new Color(155,155,155,255));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setVisible(false);
		mainPanel.setLayout(null);
		this.add(mainPanel);
		
		View.setBound(nameLabel, 0, 0, width+6, 3);
		nameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		mainPanel.add(nameLabel);

		View.setBound(typeLabel, 0.25, 3, width+6, 3);
		mainPanel.add(typeLabel);

		View.setBound(AOFLabel, 0.25, 6, width+6, 3);
		mainPanel.add(AOFLabel);

		View.setBound(rangeLabel, 0.25, 9, width+6, 3);
		mainPanel.add(rangeLabel);

		View.setBound(manaLabel, 0.25, 12, width+6, 3);
		mainPanel.add(manaLabel);

		View.setBound(actionLabel, 0.25, 15, width+6, 3);
		mainPanel.add(actionLabel);

		View.setBound(cooldownLabel, 0.25, 18, width+6, 3);
		mainPanel.add(cooldownLabel);

		View.setBound(amountLabel, 0.25, 21, width+6, 3);
		mainPanel.add(amountLabel);
		
		View.setBound(effectDuration, 0.25, 24, width+6, 3);
		mainPanel.add(effectDuration);
	}
	
	public void updatePanel(Ability a) {
		nameLabel.setText(" " + a.getName());
		typeLabel.setText("Type: " + (a instanceof DamagingAbility ? "Damaging Ability" : a instanceof HealingAbility ? "Healing Ability": "Crowd Control Ability"));
		AOFLabel.setText("AOF: " + a.getCastArea());
		rangeLabel.setText("Range: " + a.getCastRange());
		manaLabel.setText("Mana: " + a.getManaCost());
		actionLabel.setText("Actions: " + a.getRequiredActionPoints());
		cooldownLabel.setText("Cooldown: " + a.getCurrentCooldown() + "/" + a.getBaseCooldown());
		amountLabel.setText((a instanceof DamagingAbility ? "Damage Amount: " + ((DamagingAbility) a).getDamageAmount() : a instanceof HealingAbility ? "Heal Amount: " + ((HealingAbility) a).getHealAmount() : "Effect: " + ((CrowdControlAbility)a).getEffect().getName()));
		effectDuration.setText((a instanceof CrowdControlAbility ? "Effect Duration: " + ((CrowdControlAbility)a).getEffect().getDuration() : ""));
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
	public void actionPerformed(ActionEvent e) {
		play("anyInGameButton.wav");
		mainPanel.setVisible(!mainPanel.isVisible());
		this.getParent().revalidate();
		this.getParent().repaint();
	}

}
