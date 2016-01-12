package nightgames.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import nightgames.global.Global;

public class SceneButton extends JButton {
	private String choice;
	public SceneButton(String label) {
		super(label);
		setFont(new Font("Baskerville Old Face", 0, 18));
		this.choice = label;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Global.current.respond(SceneButton.this.choice);
			}
		});
	}
}
