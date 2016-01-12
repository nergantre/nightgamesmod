package nightgames.gui;

import nightgames.characters.Character;
import nightgames.combat.Encounter;
import nightgames.global.Encs;
import nightgames.global.Global;
import nightgames.trap.Trap;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class EncounterButton extends JButton{
	private Encounter enc;
	private Character target;
	private Encs choice; 
	private Trap trap;

	public EncounterButton(String label, Encounter enc, Character target,Encs choice) {
		super(label);
		setFont(new Font("Baskerville Old Face", 0, 18));
		this.enc = enc;
		this.target = target;
		this.choice = choice;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EncounterButton.this.enc.parse(EncounterButton.this.choice,EncounterButton.this.target);
				Global.getMatch().resume();
			}
		});
	}
	public EncounterButton(String label, Encounter enc, Character target,Encs choice,Trap trap) {
		super(label);
		setFont(new Font("Baskerville Old Face", 0, 18));
		this.enc = enc;
		this.target = target;
		this.choice = choice;
		this.trap = trap;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EncounterButton.this.enc.parse(EncounterButton.this.choice,EncounterButton.this.target,EncounterButton.this.trap);
				Global.getMatch().resume();
			}
		});
	}
}
