package nightgames.gui;

import java.awt.Font;

import javax.swing.JButton;

import nightgames.characters.Character;
import nightgames.combat.Encounter;
import nightgames.global.Encs;
import nightgames.global.Global;
import nightgames.trap.Trap;

public class EncounterButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6203417731879208074L;
	private Encounter	enc;
	private Character	target;
	private Encs		choice;
	private Trap		trap;

	public EncounterButton(String label, Encounter enc, Character target,
			Encs choice) {
		super(label);
		setFont(new Font("Baskerville Old Face", 0, 18));
		this.enc = enc;
		this.target = target;
		this.choice = choice;
		addActionListener(arg0 -> {
			EncounterButton.this.enc.parse(EncounterButton.this.choice,
					EncounterButton.this.target);
			Global.getMatch().resume();
		});
	}

	public EncounterButton(String label, Encounter enc, Character target,
			Encs choice, Trap trap) {
		super(label);
		setFont(new Font("Baskerville Old Face", 0, 18));
		this.enc = enc;
		this.target = target;
		this.choice = choice;
		this.trap = trap;
		addActionListener(arg0 -> {
			EncounterButton.this.enc.parse(EncounterButton.this.choice,
					EncounterButton.this.target, EncounterButton.this.trap);
			Global.getMatch().resume();
		});
	}
}
