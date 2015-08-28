package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;

public class Dissolve extends Skill {

	public Dissolve(Character self) {
		super("Dissolve", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(getSelf())&&getSelf().canAct()&&getSelf().has(Item.DisSol)&&!target.nude()&&!c.getStance().prone(getSelf())
				&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.DisSol, 1);
		if(getSelf().has(Item.Aersolizer)){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, getSelf()));
			}
			if(!target.top.isEmpty()){
				target.shred(0);
			}
			if(!target.bottom.isEmpty()){
				target.shred(1);
			}
		}
		else if(target.roll(this, c, accuracy(c))){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, getSelf()));
			}
			if(!target.top.isEmpty()){
				target.shred(0);
			}
			if(!target.bottom.isEmpty()){
				target.shred(1);
			}
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
		return new Dissolve(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.special){
			return "You pop a Dissolving Solution into your Aerosolizer and spray "+target.name()+" with a cloud of mist. She emerges from the cloud with her clothes rapidly " +
					"melting off her body.";
		}
		else if(modifier == Result.miss){
			return "You throw a Dissolving Solution at "+target.name()+", but she avoids most of it. Only a couple drops burn through her outfit.";
		}
		else{
			return "You throw a Dissolving Solution at "+target.name()+", which eats away her clothes.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		if(modifier == Result.special){
			return getSelf().name()+" inserts a bottle into the attachment on her arm. You're suddenly surrounded by a cloud of mist. Your clothes begin to disintegrate immediately.";
		}
		else if(modifier == Result.miss){
			return getSelf().name()+" splashes a bottle of liquid in your direction, but none of it hits you.";
		}
		else{
			return getSelf().name()+" covers you with a clear liquid. Your clothes dissolve away, but it doesn't do anything to your skin.";
		}
	}

	@Override
	public String describe() {
		return "Throws dissolving solution to destroy opponent's clothes";
	}

}
