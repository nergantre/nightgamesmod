package skills;

import items.Item;
import status.Winded;
import global.Global;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

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
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().behind(self)&&self.has(Item.Battery,4);
	}

	@Override
	public String describe() {
		return "A blast of light and sound with a chance to stun: 4 Battery";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.consume(Item.Battery, 4);
		if(Global.random(10)>=4){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.getStamina().empty();
			target.add(new Winded(target));
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
			return self.name()+" covers her face and points a device in your direction. Sensing danger, you shield you eyes just as the flashbang goes off.";
		}
		else{
			return self.name()+" points a device in your direction that glows slightly. A sudden flash of light disorients you and your ears ring from the blast.";
		}
	}

}
