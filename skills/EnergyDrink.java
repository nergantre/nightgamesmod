package skills;

import items.Item;
import global.Global;
import global.Modifier;
import status.Hypersensitive;
import characters.Character;

import combat.Combat;
import combat.Result;

public class EnergyDrink extends Skill {

	public EnergyDrink(Character self) {
		super("Energy Drink", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(getSelf())&&getSelf().canAct()&&getSelf().has(Item.EnergyDrink)&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public String describe() {
		return "Terrible stuff, but will make you less tired";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, getSelf()));
		}
		getSelf().heal(c, 10+Global.random(10));
		getSelf().consume(Item.EnergyDrink, 1);
	}

	@Override
	public Skill copy(Character user) {
		return new EnergyDrink(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.recovery;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You chug an energy drink and feel some of your fatigue vanish.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return target.name()+" opens up an energy drink and downs the whole can.";
	}

}
