package skills;

import status.Rewired;
import status.Shamed;
import global.Global;
import items.Item;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class ShortCircuit extends Skill {

	public ShortCircuit(Character self) {
		super("Short-Circuit", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Science)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().prone(self)&&target.nude()&&self.has(Item.Battery, 3);
	}

	@Override
	public String describe() {
		return "A blast of energy with confused your opponent's nerves so she can't tell pleasure from pain: 3 Batteries.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.consume(Item.Battery, 3);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		target.add(new Rewired(target,4+Global.random(3)));
	}

	@Override
	public Skill copy(Character user) {
		return new ShortCircuit(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You send a light electrical current through "+target.name()+"'s body, disrupting her nerve endings. She'll temporarily feel pleasure as pain and pain as pleasure.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" aims a devices at you and you feel a strange shiver run across your skin. You feel indescribably weird. She's done something to your sense of touch.";
	}

}
