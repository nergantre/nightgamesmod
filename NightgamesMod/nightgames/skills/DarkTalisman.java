package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Enthralled;
import nightgames.status.Stsflag;

public class DarkTalisman extends Skill {

	public DarkTalisman(Character self) {
		super("Dark Talisman", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return getSelf().getRank() >= 1;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (getSelf().canAct()) && (c.getStance().mobile(getSelf()))
				&& (!c.getStance().prone(getSelf()))
				&& (!target.is(Stsflag.enthralled));
	}

	@Override
	public String describe(Combat c) {
		return "Consume the mysterious talisman to control your opponent";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Talisman, 1);
		if (getSelf().human()) {
			c.write(deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(receive(c, 0, Result.normal, target));
		}
		target.add(new Enthralled(target, getSelf(), Global.random(3) + 1));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new DarkTalisman(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return "You brandish the dark talisman, which seems to glow with power. The trinket crumbles to dust, but you see the image remain in the reflection of "
				+ target.name() + "'s eyes.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return getSelf().name()
				+ " holds up a strange talisman. You feel compelled to look at the thing, captivated by its unholy nature.";
	}

}
