package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Standing;
import nightgames.stance.StandingOver;
import nightgames.status.CockBound;
import nightgames.status.Falling;

public class Carry extends Fuck {

	public Carry(String name, Character self) {
		super(name, self, 5);
	}

	public Carry(Character self) {
		super("Carry", self, 5);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Power)>=25 && !user.has(Trait.petite);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&& !target.wary()
				&& getTargetOrgan(target).isReady(target)
				&& getSelf().canAct()
				&& c.getStance().mobile(getSelf())
				&& !c.getStance().prone(getSelf())
				&& !c.getStance().prone(target)
				&& !c.getStance().facing()
				&& getSelf().getStamina().get()>=15
				&& !c.getStance().penetration(getSelf());
	}
	
	public int getMojoCost(Combat c) {
		return 40;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		String premessage = "";
		if(!getSelf().bottom.empty() && getSelfOrgan().isType("cock")) {
			if (getSelf().bottom.size() == 1) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s halfway and", getSelf().bottom.get(0).getName());
			} else if (getSelf().bottom.size() == 2) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s and %s halfway and", getSelf().bottom.get(0).getName(), getSelf().bottom.get(1).getName());
			}
		}

		premessage = Global.format(premessage, getSelf(), target);
		if(target.roll(this, c, accuracy())){
			if(getSelf().human()){
				c.write(getSelf(),premessage + deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),premessage + receive(c,0,Result.normal, getSelf()));
			}
			c.setStance(new Standing(getSelf(),target));
		}
		else{
			if(getSelf().human()) {
				c.write(getSelf(),premessage + deal(c,0,Result.miss, target));
			} else if(target.human()){
				c.write(getSelf(),premessage + receive(c,0,Result.miss, target));
			}
			getSelf().add(c, new Falling(getSelf()));
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Carry(user);
	}
	public int accuracy(){
		return 0;
	}
	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "you pick up "+target.name()+", but she flips out of your arms and manages to trip you.";
		}
		else{
			return "you scoop up "+target.name()+", lifting her into the air and simultaneously thrusting your dick into her hot depths. She lets out a noise that's " +
				"equal parts surprise and delight as you bounce her on your pole.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return Global.format("{self:subject} picks you up, but you scramble out of {self:posessive} grip before {self:pronoun} can do anything. Moreover, you manage to trip her while she's distracted.", getSelf(), target);
		} else {
			return Global.format("{self:subject} scoops you up in {self:possessive} powerful arms and simultaneously thrusts {self:posessive} {self:body-part:cock} into your {other:body-part:pussy}.", getSelf(), target);
		}
	}

	@Override
	public String describe() {
		return "Picks up opponent and penetrates her: Mojo 10.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "pussy";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "cock";
	}
}
