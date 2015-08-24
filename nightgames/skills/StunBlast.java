package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Falling;
import nightgames.status.Winded;

public class StunBlast extends Skill {

	public StunBlast(Character self) {
		super("Stun Blast", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Science)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&c.getStance().front(getSelf())&&getSelf().has(Item.Battery,4);
	}

	@Override
	public String describe() {
		return "A blast of light and sound with a chance to stun: 4 Battery";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Battery, 4);
		if(Global.random(10)>=4){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.getStamina().empty();
			target.add(c, new Falling(target));
			target.add(c, new Winded(target));
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
		return new StunBlast(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You overload the emitter on your arm, but "+target.name()+" shields her face to avoid the flash.";
		}
		else{
			return "You overload the emitter on your arm, duplicating the effect of a flashbang. "+target.name()+" staggers as the blast disorients her.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" covers her face and points a device in your direction. Sensing danger, you shield you eyes just as the flashbang goes off.";
		}
		else{
			return getSelf().name()+" points a device in your direction that glows slightly. A sudden flash of light disorients you and your ears ring from the blast.";
		}
	}

}
