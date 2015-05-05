package skills;

import stance.Jumped;
import stance.Standing;
import stance.StandingOver;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.body.BodyPart;

import combat.Combat;
import combat.Result;

public class ReverseCarry extends Carry {
	public ReverseCarry(Character self) {
		super("Jump", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Power)>=20 && user.hasPussy();
	}

	@Override
	public BodyPart getSelfOrgan() {
		return self.body.getRandomPussy();
	}

	@Override
	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandomCock();
	}

	@Override
	public void resolve(Combat c, Character target) {
		String premessage = "";
		self.spendMojo(c, getMojoSpent());
		if (self.bottom.size() == 1) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and", self.bottom.get(0).name());
		} else if (self.bottom.size() == 2) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s and %s to the side and", self.bottom.get(0).name(), self.bottom.get(1).name());
		}

		premessage = Global.format(premessage, self, target);
		if(target.roll(this, c, accuracy())){
			if(self.human()){
				c.write(self,premessage + deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,premessage + receive(c,0,Result.normal, self));
			}

			c.setStance(new Jumped(self,target));
		}
		else{
			if(self.human()) {
				c.write(self,premessage + deal(c,0,Result.miss, target));
			} else if(target.human()){
				c.write(self,premessage + receive(c,0,Result.miss, target));
			}
			c.setStance(new StandingOver(target, self));
		}
	}

	@Override
	public Skill copy(Character user) {
		return new ReverseCarry(user);
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "you leap into "+target.possessivePronoun()+" arms, but she deposits you back onto the floor.";
		}
		else{
			return Global.format("you leap into {other:possessive} arms, impaling yourself onto her {other:body-part:cock} "
								+ ". She lets out a noise that's equal parts surprise and delight as you bounce on her pole.", self, target);
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" jumps onto you, but you deposit her back on the floor.";
		}
		else{
			return self.name()+" leaps into your arms and impales herself on your cock. She wraps her legs around your torso and you quickly support her so she doesn't " +
				"fall and injure herself or you.";
		}
	}

	@Override
	public String describe() {
		return "Jump into your opponent's arms and impale yourself on her cock: Mojo 10.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
