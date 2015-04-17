package skills;

import stance.Position;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Piston extends Thrust {
	public Piston(Character self) {
		super("Piston", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Seduction)>=18;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction)>=18;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().canthrust(self)&&c.getStance().penetration(self);
	}

	@Override
	public int getMojoBuilt() {
		return 20;
	}

	@Override
	public int[] getDamage(Character target, Position stance) {
		int results[] = new int[2];

		int m = 12 + Global.random(8);
		int mt = 8 + Global.random(5);
		if(self.has(Trait.experienced)){
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
			return Global.format("{self:SUBJECT-ACTION:bounce|bounces} on {other:name-possessive} cock, relentlessly driving you both towards orgasm.", self, target);
		} else {
			return "You rapidly pound your dick into "+target.name()+"'s pussy. Her pleasure filled cries are proof that you're having an effect, but you're feeling it " +
					"as much as she is.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.anal){
			return self.name()+" relentlessly pegs you in the ass as you groan and try to endure the sensation.";
		}
		else if(modifier == Result.upgrade){
			return self.name()+" pistons into you while pushing your shoulders on the ground. While her Strap-On stimulates your prostate, "
					+self.name()+"'s tits are shaking above your head.";
		} else if (modifier == Result.reverse) {
			return self.name()+" bounces on your cock, relentlessly driving you both toward orgasm.";
		} else{
			return Global.format("{self:SUBJECT-ACTION:rapidly pound|rapidly pounds} {self:possessive} {self:body-part:cock} into {other:possessive} {other:body-part:pussy}, "+
								"relentlessly driving you both toward orgasm", self, target);
		}
	}

	@Override
	public String describe() {
		return "Fucks opponent without holding back. Very effective, but dangerous";
	}

	@Override
	public String getName(Combat c) {
		if (c.getStance().inserted(self)) {
			return "Piston";
		} else {
			return "Bounce";
		}
	}
}
