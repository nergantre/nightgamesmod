package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;

public class Tighten extends Thrust {
	public Tighten(Character self) {
		super("Tighten", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=26 || user.has(Trait.tight);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canRespond()&&c.getStance().penetratedBy(getSelf(), target)&&c.getStance().havingSexNoStrapped()&&target.hasDick();	
	}

	@Override
	public int[] getDamage(Combat c, Character target) {
		int[] result = new int[2];

		int m = 5 + Global.random(10) + Math.min(getSelf().get(Attribute.Power)/3, 20);
		result[0] = m;
		result[1] = 0;

		return result;
	}
	
	@Override
	public int getMojoBuilt(Combat c) {
		return 0;
	}

	@Override
	public Skill copy(Character user) {
		return new Tighten(user);
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (c.getStance().en == Stance.anal) {
			return Global.format("{self:SUBJECT-ACTION:rhythmically squeeze|rhythmically squeezes} {self:possessive} {self:body-part:ass} around {other:possessive} dick, milking {other:direct-object} for all that {self:subject-action:are|is} worth.", getSelf(), target);
		} else {
			return Global.format("{self:SUBJECT-ACTION:give|gives} {other:direct-object} a seductive wink and suddenly {self:possessive} {self:body-part:pussy} squeezes around {other:possessive} {other:body-part:cock} as though it's trying to milk {other:direct-object}.", getSelf(), target);
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return deal(c, damage, modifier, target);
	}

	@Override
	public String describe(Combat c) {
		return "Squeeze opponent's dick, no pleasure to self";
	}
	
	@Override
	public String getName(Combat c) {
		return "Tighten";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
