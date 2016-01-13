package nightgames.gui;

import java.util.Observable;

import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.skills.Skill;

public class NullGUI extends GUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1739250786661411957L;

	public NullGUI() {
		setVisible(false);
	}

	@Override
	public Combat beginCombat(Character p1, Character p2) {
		combat = new Combat(p1, p2, p1.location());
		combat.addObserver(this);
		return combat;
	}

	@Override
	public void populatePlayer(Player player) {

	}

	@Override
	public void clearText() {
	}

	@Override
	public void message(String text) {
	}

	@Override
	public void clearCommand() {
	}

	@Override
	public void addSkill(Skill action, Combat com) {
	}

	@Override
	public void setPlayer(Player player) {
	}

	@Override
	public void next(Combat combat) {
	}

	public void promptFF(IEncounter enc) {
	}

	@Override
	public void promptAmbush(IEncounter enc, Character target) {
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (combat != null) {
			if (combat.phase == 0 || combat.phase == 2) {
				combat.next();
			}
		}
	}

	@Override
	public void endCombat() {
		combat = null;
	}
}
