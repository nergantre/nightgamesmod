package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import skills.Skill;
import skills.Tactics;

import combat.Combat;

import java.awt.Color;
import java.awt.Font;


public class SkillButton extends JButton{
	protected Skill action;
	protected Combat combat;
	public SkillButton(Skill action, Combat c){
		super(action.getName(c));
		setOpaque(true);
		setBorderPainted(false);
		setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
		this.action=action;
		setToolTipText(action.describe());
		if(action.type(c)==Tactics.damage){
			setBackground(new Color(150,0,0));
			setForeground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.pleasure){
			setBackground(Color.PINK);
		}
		else if(action.type(c)==Tactics.fucking){
			setBackground(new Color(255,100,200));
		}
		else if(action.type(c)==Tactics.positioning){
			setBackground(new Color(0,100,0));
			setForeground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.stripping){
			setBackground(new Color(0,100,0));
			setForeground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.debuff){
			setBackground(Color.CYAN);
		}
		else if(action.type(c)==Tactics.recovery||action.type(c)==Tactics.calming){
			setBackground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.summoning){
			setBackground(Color.YELLOW);
		}
		else{
			setBackground(new Color(200,200,200));
		}
		this.combat=c;
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SkillButton.this.combat.act(SkillButton.this.action.user(), SkillButton.this.action);
			}			
		});
	}
}
