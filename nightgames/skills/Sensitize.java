package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;

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
		return c.getStance().mobile(getSelf())&&getSelf().canAct()&&getSelf().has(Item.SPotion)&&target.nude()&&!c.getStance().prone(getSelf())&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public String describe() {
		return "Makes your opponent hypersensitive";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.SPotion, 1);
		if(getSelf().has(Item.Aersolizer)){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, getSelf()));
			}
			target.add(c, new Hypersensitive(target));
		}
		else if(target.roll(this, c, accuracy())){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, getSelf()));
			}
			target.add(c, new Hypersensitive(target));
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
			return "You throw a bottle of sensitivity elixir at "+target.name()+", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
		}
		else{
			return "You throw a sensitivity potion at "+target.name()+". You see her skin flush as it takes effect.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.special){
			return getSelf().name()+" inserts a bottle into the attachment on her arm. You're suddenly surrounded by a cloud of minty gas. Your skin becomes hot, but goosebumps appear anyway. " +
					"Even the air touching your skin makes you shiver.";
		}
		else if(modifier == Result.miss){
			return getSelf().name()+" splashes a bottle of liquid in your direction, but none of it hits you.";
		}
		else{
			return getSelf().name()+" throws a bottle of strange liquid at you. The skin it touches grows hot and oversensitive.";
		}
	}

}
