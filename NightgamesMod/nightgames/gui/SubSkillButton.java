package nightgames.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

import nightgames.combat.Combat;
import nightgames.skills.Skill;

public class SubSkillButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3177604366435328960L;
	protected Skill		action;
	protected Combat	combat;

	public SubSkillButton(final Skill action, final String choice, Combat c) {
		super(choice);
		setOpaque(true);
		setBorderPainted(false);
		setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
		this.action = action;
		setBackground(new Color(200, 200, 200));
		combat = c;
		addActionListener(arg0 -> combat.act(action.user(), action, choice));
	}
}
