package nightgames.gui;

import java.awt.Font;

import javax.swing.JButton;

import nightgames.global.Global;

public class SaveButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5665392145091151054L;

	public SaveButton() {
		super("Save");
		setFont(new Font("Baskerville Old Face", 0, 18));
		addActionListener(arg0 -> Global.save(false));
	}
}