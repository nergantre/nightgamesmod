package skills;

import stance.Jumped;
import stance.Standing;
import stance.StandingOver;
import status.Falling;
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
		return getSelf().body.getRandomPussy();
	}

	@Override
	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandomCock();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		String premessage = "";
		if (getSelf().bottom.size() == 1) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and", getSelf().bottom.get(0).name());
		} else if (getSelf().bottom.size() == 2) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s and %s to the side and", getSelf().bottom.get(0).name(), getSelf().bottom.get(1).name());
		}

		premessage = Global.format(premessage, getSelf(), target);
		if(target.roll(this, c, accuracy())){
			if(getSelf().human()){
				c.write(getSelf(),premessage + deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),premessage + receive(c,0,Result.normal, getSelf()));
			}

			c.setStance(new Jumped(getSelf(),target));
		}
		else{
			if(getSelf().human()) {
				c.write(getSelf(),premessage + deal(c,0,Result.miss, target));
			} else if(target.human()){
				c.write(getSelf(),premessage + receive(c,0,Result.miss, target));
			}
			target.add(c, new Falling(target));
			return false;
		}
		return true;
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
								+ ". She lets out a noise that's equal parts surprise and delight as you bounce on her pole.", getSelf(), target);
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" jumps onto you, but you deposit her back on the floor.";
		}
		else{
			return getSelf().name()+" leaps into your arms and impales herself on your cock. She wraps her legs around your torso and you quickly support her so she doesn't " +
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
