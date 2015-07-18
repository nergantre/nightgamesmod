package skills;

import java.util.ArrayList;
import java.util.Collection;

import items.Item;
import items.ItemEffect;
import global.Global;
import global.Modifier;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class UseSemen extends Skill {
	public UseSemen(Character self) {
		super("Drink Semen Bottle", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		boolean hasItems = getSelf().has(Item.semen);
		return hasItems&&getSelf().canAct()&&getSelf().has(Trait.succubus)&&c.getStance().mobile(getSelf())&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public void resolve(Combat c, Character target) {
		Item used = Item.semen;
		boolean eventful = false;
		c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:take|takes} out a bottle of milky white semen and {self:action:gulp|gulps} it down in one breath.", getSelf(), target));
		for (ItemEffect e : used.getEffects()) {
			eventful = e.use(c, getSelf(), target, used) || eventful;
		}
		if (!eventful) {
			c.write("...But nothing happened.");
		}
		getSelf().consume(used, 1);
	}

	@Override
	public Skill copy(Character user) {
		return new UseSemen(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String describe() {
		return "Drink a bottle of semen";
	}

	@Override
	public boolean makesContact() {
		return false;
	}
}
