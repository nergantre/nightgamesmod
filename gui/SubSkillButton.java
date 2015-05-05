package gui;
import global.Global;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import characters.Player;

import skills.Skill;
import skills.Tactics;

import combat.Combat;

import java.awt.Color;
import java.awt.Font;


public class SubSkillButton extends JButton{
	protected Skill action;
	protected Combat combat;
	public SubSkillButton(final Skill action, final String choice, Combat c){
		super(choice);
		setOpaque(true);
		setBorderPainted(false);
		setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
		this.action=action;
		setBackground(new Color(200,200,200));
		this.combat=c;
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SubSkillButton.this.combat.act(action.user(), action, choice);
			}
		});
	}
}
