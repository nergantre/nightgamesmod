package nightgames.characters.body;

import org.json.simple.JSONObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class MouthPussyPart extends MouthPart {
	/**
	 * 
	 */
	public static BodyPartMod MouthPussyMod = () -> "MouthPussyMod";
	public MouthPussyPart() {
		super("mouth pussy", "When she opens her mouth, you can see soft pulsating folds lining her inner mouth, tailor made to suck cocks.", .5, 1.2, 1.5, true, "a ");
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		bonus = super.applyBonuses(self, opponent, target, damage, c);
		return bonus;
	}

	@Override
	public String getFluids(Character c) {
		return "juices";
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = super.getPleasure(self, target);
		if (target != null && target.isType("cock")) {
			pleasureMod += 2;
		} else if (target != null && target.isType("mouth")) {
			pleasureMod += 1;
		}
		return pleasureMod;
	}

	@Override
	public BodyPart loadFromDict(JSONObject dict) {
		try {
		GenericBodyPart part = new MouthPussyPart();
			return part;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean isErogenous() {
		return true;
	}

	@Override
	public boolean isVisible(Character c) {
		return true;
	}

	@Override
	public BodyPartMod getMod() {
		return MouthPussyMod;
	}
}
