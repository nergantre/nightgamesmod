package skills;

import items.Item;
import global.Global;
import global.Modifier;
import status.Hypersensitive;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Sensitize extends Skill {

	public Sensitize(Character self) {
		super("Sensitivity Potion", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(self)&&self.canAct()&&self.has(Item.SPotion)&&target.nude()&&!c.getStance().prone(self)&&(!self.human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public String describe() {
		return "Makes your opponent hypersensitive";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.consume(Item.SPotion, 1);
		if(self.has(Item.Aersolizer)){
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.special, self));
			}
			target.add(new Hypersensitive(target));
		}
		else if(target.roll(this, c, accuracy())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, self));
			}
			target.add(new Hypersensitive(target));
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
		return new Sensitize(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.special){
			return "You pop a sensitivity potion into your Aerosolizer and spray "+target.name()+" with a cloud of mist. She shivers as it takes hold and heightens her " +
					"sense of touch.";
		}
		else if(modifier == Result.miss){
			return "You throw a bottle of sensitivity elixer at "+target.name()+", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
		}
		else{
			return "You thow a sensitivity potion at "+target.name()+". You see her skin flush as it takes effect.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.special){
			return self.name()+" inserts a bottle into the attachment on her arm. You're suddenly surrounded by a cloud of minty gas. Your skin becomes hot, but goosebumps appear anyway. " +
					"Even the air touching your skin makes you shiver.";
		}
		else if(modifier == Result.miss){
			return self.name()+" splashes a bottle of liquid in your direction, but none of it hits you.";
		}
		else{
			return self.name()+" throws a bottle of strange liquid at you. The skin it touches grows hot and oversensitive.";
		}
	}

}
