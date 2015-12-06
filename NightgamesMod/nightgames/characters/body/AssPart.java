package nightgames.characters.body;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Stsflag;

public class AssPart extends GenericBodyPart {
	/**
	 * 
	 */
	public static AssPart generic = new AssPart("ass", 0, 1.2, 1);
	public double size;
	public static double SIZE_SMALL = 0;
	public static double SIZE_NORMAL = 1;
	public static double SIZE_BIG = 2;
	public static double SIZE_HUGE = 3;

	public AssPart(String desc, String longDesc, double hotness,
			double pleasure, double sensitivity, double size, boolean notable) {
		super(desc, longDesc, hotness, pleasure, sensitivity, notable, "ass",
				"a ");
		this.size = size;
	}

	public AssPart(String desc, double hotness, double pleasure,
			double sensitivity) {
		super(desc, "", hotness, pleasure, sensitivity, false, "ass", "a ");
		this.size = SIZE_NORMAL;
	}

	@Override
	public double getFemininity(Character c) {
		return size - SIZE_NORMAL;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = super.getPleasure(self, target);
		pleasureMod += self.has(Trait.analTraining1) ? .5 : 0;
		pleasureMod += self.has(Trait.analTraining2) ? .7 : 0;
		pleasureMod += self.has(Trait.analTraining3) ? .7 : 0;
		return pleasureMod;
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (self.has(Trait.oiledass)) {
			c.write(self, Global.format("{self:NAME-POSSESSIVE} naturally oiled asshole swallows {other:name-possessive} cock with ease.", self, opponent));
			bonus += 5;
		}

		if (self.has(Trait.tight) || self.has(Trait.holecontrol)) {
			String desc = "";
			if (self.has(Trait.tight)) {desc += "powerful ";}
			if (self.has(Trait.holecontrol)) {desc += "well-trained ";}
			c.write(self, Global.format("{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc + "sphincter muscles to milk {other:name-possessive} cock, adding to the pleasure.", self, opponent));
			bonus += (self.has(Trait.tight) && self.has(Trait.holecontrol)) ? 10 : 5;
			if (self.has(Trait.tight))
				opponent.pain(c, Math.min(30, self.get(Attribute.Power)));
		}
		return bonus;
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
		if (self.has(Trait.autonomousAss)) {
			c.write(self, Global.format("{self:NAME-POSSESSIVE} " + fullDescribe(self) + " churns against {other:name-possessive} cock, " +
					"seemingly with a mind of its own. Her internal muscles feel like a hot fleshy hand inside her asshole, jerking {other:possessive} shaft.", self, opponent));
			opponent.body.pleasure(self, this, otherOrgan, 10, c);
		}
	}

	@Override
	public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (opponent.has(Trait.asshandler) || opponent.has(Trait.anatomyknowledge)) {
			c.write(opponent,
					Global.format(
							"{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} ass causes {self:subject} to shudder uncontrollably.",
							self, opponent));
			if (opponent.has(Trait.asshandler)) {
				bonus += 5;
			}
			if (opponent.has(Trait.anatomyknowledge)) {
				bonus += 5;
			}
		}
		return bonus;
	}

	@Override
	public boolean isReady(Character c) {
		return c.has(Trait.oiledass) || c.is(Stsflag.oiled);
	}

	@Override
	public String getFluids(Character c) {
		if (c.has(Trait.oiledass)) {
			return "oils";
		} else {
			return "";
		}
	}

	@Override
	public boolean isErogenous() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToDict() {
		JSONObject res = super.saveToDict();
		res.put("size", size);
		return res;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BodyPart loadFromDict(JSONObject dict) {
		try {
			GenericBodyPart part = new AssPart(
					(String) dict.get("desc"),
					(String) dict.get("descLong"),
					((Number) dict.get("hotness")).doubleValue(),
					((Number) dict.get("pleasure")).doubleValue(),
					((Number) dict.get("sensitivity")).doubleValue(),
					((Number) dict.getOrDefault("size", Double.valueOf(SIZE_NORMAL))).doubleValue(),
					false);
			return part;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public double priority(Character c) {
		return (c.has(Trait.tight) ? 1 : 0)+ (c.has(Trait.holecontrol) ? 1 : 0) + (c.has(Trait.oiledass) ? 1 : 0) + (c.has(Trait.autonomousAss) ? 4 : 0);
	}

}
