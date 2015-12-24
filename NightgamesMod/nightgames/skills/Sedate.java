package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;

public class Sedate extends Skill {

	public Sedate(Character self) {
		super("Sedate", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(getSelf())&&getSelf().canAct()&&getSelf().has(Item.Sedative)&&!c.getStance().prone(getSelf());
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Sedative, 1);
		if(getSelf().has(Item.Aersolizer)){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, target));
			}
			target.weaken(c, 30);
			target.loseMojo(c, 25);
		}
		else if(target.roll(this, c, accuracy(c))){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.weaken(c, 30);
			target.loseMojo(c, 25);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Sedate(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.special){
			return "You pop a sedative into your Aerosolizer and spray "+target.name()+" with a cloud of mist. She stumbles out of the cloud looking drowsy and unfocused.";
		}
		else if(modifier == Result.miss){
			return "You throw a bottle of sedative at "+target.name()+", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
		}
		else{
			return "You through a bottle of sedative at "+target.name()+". She stumbles for a moment, trying to clear the drowsiness from her head.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.special){
			return getSelf().name()+" inserts a bottle into the attachment on her arm. You're suddenly surrounded by a cloud of dense fog. The fog seems to fill your head and your body feels heavy.";
		}
		else if(modifier == Result.miss){
			return getSelf().name()+" splashes a bottle of liquid in your direction, but none of it hits you.";
		}
		else{
			return getSelf().name()+" hits you with a flask of liquid. Even the fumes make you feel sluggish and your limbs become heavy.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Throw a sedative at your opponent, weakening " + c.getOther(getSelf()).directObject();
	}
}
