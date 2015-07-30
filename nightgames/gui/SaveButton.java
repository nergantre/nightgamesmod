package nightgames.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import nightgames.global.Global;

public class SaveButton extends JButton {

	public SaveButton() {
		super("Save");
		setFont(new Font("Baskerville Old Face", 0, 18));
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Global.save(false);
			}
		});
	}
}