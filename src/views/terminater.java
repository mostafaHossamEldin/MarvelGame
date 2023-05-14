package views;
import javax.swing.*;
import java.awt.event.*;
public class terminater extends WindowAdapter {

	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

}
