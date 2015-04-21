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
		super("ReverseCarry", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Power)>=20 && self.hasPussy();
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Power)>=20 && user.hasPussy();
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
		if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, self));
			}

			c.setStance(new Jumped(self,target));
		}
		else{
			if(self.human()) {
				c.write(self,deal(c,0,Result.miss, target));
			} else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
			c.setStance(new StandingOver(target, self));
		}
		self.spendMojo(getMojoSpent());
	}

	@Override
	public Skill copy(Character user) {
		return new ReverseCarry(user);
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You leap into "+target.possessivePronoun()+" arms, but she deposits you back onto the floor.";
		}
		else{
			return Global.format("You leap into {other:possessive} arms, impaling yourself onto her {other:body-part:cock} "
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
