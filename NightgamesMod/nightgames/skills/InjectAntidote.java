package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class InjectAntidote extends Skill {
	public InjectAntidote(Character self) {
		super("Inject Antidote", self);
	}

	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Medicine) >= 7;
	}

	public boolean usable(Combat c, Character target) {
		return (c.getStance().mobile(this.getSelf()))
				&& (this.getSelf().canAct())
				&& getSelf().has(Item.MedicalSupplies, 1);
	}

	@Override
	public int getMojoCost(Combat c) {
		return 10;
	}

	public boolean resolve(Combat c, Character target) {
		if (this.getSelf().human()) {
			c.write(getSelf(),deal(c, 0, Result.normal, target));
		} else {
			c.write(getSelf(),receive(c, 0, Result.normal, this.getSelf()));
		}
		getSelf().calm(c, getSelf().getArousal().max() / 10);
		getSelf().purge(c);
		getSelf().consume(Item.MedicalSupplies, 1);
		return true;
	}

	public Skill copy(Character user) {
		return new InjectAntidote(user);
	}

	public Tactics type(Combat c) {
		return Tactics.recovery;
	}

	public String deal(Combat c, int damage, Result modifier, Character target) {
		return Global.format("You inject yourself with an antidote. The drug quickly purges any foreign influence from your system.",
				getSelf(), target);
	}

	public String receive(Combat c, int damage, Result modifier, Character target) {
		return Global.format("{self:SUBJECT} jabs {self:reflective} with a needle, sighing as {self:pronoun} pushes the needle down. Before your eyes, {self:possessive} entire bodily system is purged of all factors, both begin and malign.",
				getSelf(), target);
	}

	public String describe(Combat c) {
		return "Injects yourself with an pancea";
	}
}
