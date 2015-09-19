package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.status.ArmLocked;
import nightgames.status.LegLocked;
import nightgames.status.Stsflag;

public class SubmissiveHold extends Skill {
	public SubmissiveHold(Character self) {
		super("Submissive Hold", self);
	}

	@Override
	public float priorityMod(Combat c) {
		return getSelf().has(Trait.submissive) ? 4 : 2;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction) > 15 && user.get(Attribute.Power) >= 15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().sub(getSelf()) && (c.getStance().inserted(getSelf()) || c.getStance().inserted(target))
				&& getSelf().canSpend(getMojoCost(c)) && !target.is(Stsflag.armlocked)&& !target.is(Stsflag.leglocked);
	}

	public int getMojoCost(Combat c) {
		return 10;
	}

	@Override
	public String describe(Combat c) {
		return "Holds your opponent in position";
	}
	
	private boolean isArmLock(Position p) {
		if (p.en == Stance.missionary) {
			return false;
		}
		return true;
	}

	@Override
	public String getLabel(Combat c){
		if(isArmLock(c.getStance())){
			return "Hand Lock";
		} else {
			return "Leg Lock";
		}
	}
	
	@Override
	public Skill copy(Character user) {
		return new SubmissiveHold(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (isArmLock(c.getStance())) {
			return Global.format("You entwine {other:name-possessive} fingers with your own, holding her in position.", getSelf(), target);
		} else {
			return Global.format("You embrace {other:name} and wrap your legs around her waist, holding her inside you.", getSelf(), target);
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (isArmLock(c.getStance())) {
			return Global.format("{self:SUBJECT} entwines your fingers with her own, holding you in position.", getSelf(), target);
		} else {
			return Global.format("{self:SUBJECT} embraces you and wraps her lithesome legs around your waist, holding you inside her.", getSelf(), target);
		}
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		if (isArmLock(c.getStance())) {
			target.add(c, new ArmLocked(target, 4 * getSelf().get(Attribute.Power)));
		} else {
			target.add(c, new LegLocked(target, 4 * getSelf().get(Attribute.Power)));
		}
		return true;
	}
}
