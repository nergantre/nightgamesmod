package nightgames.gui;

import java.awt.Font;

import javax.swing.JButton;

import nightgames.global.Global;

public class SceneButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4333729595458261030L;
	private String choice;

	public SceneButton(String label) {
		super(label);
		setFont(new Font("Baskerville Old Face", 0, 18));
		choice = label;
		addActionListener(arg0 -> Global.current.respond(choice));
	}
}
