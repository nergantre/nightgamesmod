package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Rewired;
import nightgames.status.Shamed;

public class ShortCircuit extends Skill {

	public ShortCircuit(Character self) {
		super("Short-Circuit", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Science)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&target.nude()&&getSelf().has(Item.Battery, 3);
	}

	@Override
	public String describe(Combat c) {
		return "Fire a  blast of energy to confuse your opponent's nerves so she can't tell pleasure from pain: 3 Batteries.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Battery, 3);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		target.add(c, new Rewired(target,4+Global.random(3)));
		return true;
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
		return getSelf().name()+" aims a devices at you and you feel a strange shiver run across your skin. You feel indescribably weird. She's done something to your sense of touch.";
	}

}
