package views;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.world.Champion;

public class Cell extends JButton implements ActionListener {
	private static View view;
	private Champion champ;
	private int n;
	private Image img;
	private JLabel nameLabel;

	public Cell(Champion c, int i, View v,int x, int y) {
		super();
		champ = c;
		n = i;
		view = v;
		this.setBackground(Color.GRAY);
		nameLabel = new JLabel(c.getName(), SwingConstants.CENTER);
		nameLabel.setFont(new Font("",Font.BOLD,22));
		View.setBound(nameLabel,0,0,x,3);
		this.setLayout(null);
		this.add(nameLabel);
		addActionListener(this);
		try {
			img = ImageIO.read(getClass().getClassLoader().getResource(c.getName() + ".png"));
		} catch (Exception e) {
			try {
				img = ImageIO.read(getClass().getClassLoader().getResource("vegeta.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public Image getImg() {
		return img;
	}

	public void actionPerformed(ActionEvent e) {
		view.play("anyInGameButton.wav");
		if(e.getActionCommand().length() == 3) {
			view.choiceOfLeader(Integer.parseInt(e.getActionCommand().charAt(2)+""), e.getActionCommand().substring(0, 2));
		}
		else {
			view.selectChamp(n);
		}
	}
	
	public Champion getChamp() {
		return champ;
	}

	public int getN() {
		return n;
	}
}
