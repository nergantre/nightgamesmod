package skills;

import items.Item;
import global.Global;
import global.Modifier;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Sedate extends Skill {

	public Sedate(Character self) {
		super("Sedate", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(self)&&self.canAct()&&self.has(Item.Sedative)&&!c.getStance().prone(self)&&(!self.human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.consume(Item.Sedative, 1);
		if(self.has(Item.Aersolizer)){
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.special, target));
			}
			target.weaken(c, 30);
			target.loseMojo(c, 10);
		}
		else if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.weaken(c, 30);
			target.loseMojo(c, 10);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
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
			return self.name()+" inserts a bottle into the attachment on her arm. You're suddenly surrounded by a cloud of dense fog. The fog seems to fill your head and your body feels heavy.";
		}
		else if(modifier == Result.miss){
			return self.name()+" splashes a bottle of liquid in your direction, but none of it hits you.";
		}
		else{
			return self.name()+" hits you with a flask of liquid. Even the fumes make you feel sluggish and your limbs become heavy.";
		}
	}

	@Override
	public String describe() {
		return "Throw sedative at opponent, weakening her";
	}
}
