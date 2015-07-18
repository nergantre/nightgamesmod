package skills;

import items.Item;
import global.Global;
import global.Modifier;
import status.Oiled;
import status.Stsflag;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Lubricate extends Skill {

	public Lubricate(Character self) {
		super("Lubricate", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(getSelf())&&getSelf().canAct()&&getSelf().has(Item.Lubricant)&&target.nude()&&!target.is(Stsflag.oiled)&&!c.getStance().prone(getSelf())
				&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		target.add(new Oiled(target));
		getSelf().consume(Item.Lubricant, 1);
	}

	@Override
	public Skill copy(Character user) {
		return new Lubricate(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You cover "+target.name()+" with an oily Lubricant.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" throws an oily liquid at you. The liquid clings to you and makes your whole body slippery.";
	}

	@Override
	public String describe() {
		return "Oil up your opponent, making her easier to pleasure";
	}

}
