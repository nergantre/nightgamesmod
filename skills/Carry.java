package skills;

import global.Global;
import stance.Standing;
import status.CockBound;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Carry extends Fuck {

	public Carry(String name, Character self) {
		super(name, self);
	}

	public Carry(Character self) {
		super("Carry", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Power)>=25 && !self.has(Trait.petite);
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Power)>=25 && !user.has(Trait.petite);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&& !target.wary()
				&& getTargetOrgan(target).isReady(target)
				&& self.canAct()
				&& c.getStance().mobile(self)
				&& !c.getStance().prone(self)
				&& !c.getStance().prone(target)
				&& self.getStamina().get()>=15
				&& self.canSpend(getMojoSpent())
				&& !c.getStance().penetration(self);
	}
	
	public int getMojoSpent() {
		return 25;
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
			c.setStance(new Standing(self,target));
		}
		else{
			if(self.human()) {
				c.write(self,deal(c,0,Result.miss, target));
			} else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
		self.spendMojo(getMojoSpent());
	}

	@Override
	public Skill copy(Character user) {
		return new Carry(user);
	}
	public int accuracy(){
		return 2;
	}
	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You pick up "+target.name()+", but she scrambles out of your arms.";
		}
		else{
			return "You scoop up "+target.name()+", lifting her into the air and simultaneously thrusting your dick into her hot depths. She lets out a noise that's " +
				"equal parts surprise and delight as you bounce her on your pole.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return Global.format("{self:subject} picks you up, but you scramble out of {self:posessive} grip before {self:pronoun} could do anything.", self, target);
		} else {
			return Global.format("{self:subject} scoops you up in {self:possessive} powerful arms and simultaneously thrusts {self:posessive} {self:body-part:cock} into your {other:body-part:pussy}.", self, target);
		}
	}

	@Override
	public String describe() {
		return "Picks up opponent and penetrates her: Mojo 10.";
	}

}
