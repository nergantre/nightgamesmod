package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Position;

public class Piston extends Thrust {
	public Piston(Character self) {
		super("Piston", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction)>=18;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().canthrust(getSelf())&&(c.getStance().penetration(getSelf())||c.getStance().penetration(target));
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 0;
	}

	@Override
	public int[] getDamage(Character target, Position stance) {
		int results[] = new int[2];

		int m = 12 + Global.random(8);
		int mt = 8 + Global.random(5);
		if(getSelf().has(Trait.experienced)){
			mt = mt * 3 / 4;
		}
		mt = Math.max(1, mt);
		results[0] = m;
		results[1] = mt;

		return results;
	}

	@Override
	public Skill copy(Character user) {
		return new Piston(user);
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.anal||modifier == Result.upgrade){
			return "You pound "+target.name()+" in the ass. She whimpers in pleasure and can barely summon the strength to hold herself off the floor.";
		} else if (modifier == Result.reverse) {
			return Global.format("{self:SUBJECT-ACTION:bounce|bounces} on {other:name-possessive} cock, relentlessly driving you both towards orgasm.", getSelf(), target);
		} else {
			return "You rapidly pound your dick into "+target.name()+"'s pussy. Her pleasure filled cries are proof that you're having an effect, but you're feeling it " +
					"as much as she is.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.anal){
			return getSelf().name()+" relentlessly pegs you in the ass as you groan and try to endure the sensation.";
		}
		else if(modifier == Result.upgrade){
			return getSelf().name()+" pistons into you while pushing your shoulders on the ground. While her Strap-On stimulates your prostate, "
					+getSelf().name()+"'s tits are shaking above your head.";
		} else if (modifier == Result.reverse) {
			return getSelf().name()+" bounces on your cock, relentlessly driving you both toward orgasm.";
		} else{
			return Global.format("{self:SUBJECT-ACTION:rapidly pound|rapidly pounds} {self:possessive} {self:body-part:cock} into {other:possessive} {other:body-part:pussy}, "+
								"relentlessly driving you both toward orgasm", getSelf(), target);
		}
	}

	@Override
	public String describe() {
		return "Fucks opponent without holding back. Very effective, but dangerous";
	}

	@Override
	public String getName(Combat c) {
		if (c.getStance().inserted(getSelf())) {
			return "Piston";
		} else {
			return "Bounce";
		}
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
